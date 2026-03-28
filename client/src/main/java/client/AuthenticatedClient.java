package client;

import chess.ChessGame;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class AuthenticatedClient extends BaseClient {

    public AuthenticatedClient(ServerFacade server, String authToken, String username) throws ClientException {
        super(server, String.format("%s%s Hello, %s!", SET_TEXT_COLOR_WHITE, WHITE_QUEEN, username));
        this.authToken = authToken;
        this.username = username;
    }

    public void printPrompt() {
        System.out.printf("\n%s[LOGGED_IN as %s] >>> %s", SET_TEXT_COLOR_LIGHT_GREY, username, SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {

        try {

            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> playGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };

        } catch (ClientException ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws ClientException {
        server.logout(authToken);
        return "quit";
    }

    public String createGame(String... params) throws ClientException {

        if (params.length >= 1) {

            String gameName = params[0];
            CreateGameResult createGameResult = server.createGame(authToken, new CreateGameRequest(gameName));

            return String.format("Game \"%s\" has been created with ID: %d.", gameName, createGameResult.gameID());
        }

        throw new ClientException("Error: Expected: <gameName>");
    }

    public String listGames() throws ClientException {

        ListGamesResult listGamesResult = server.listGames(authToken);
        StringBuilder builder = new StringBuilder("Games:\n");

        for (GameData gameData : listGamesResult.games()) {

            builder.append("\t");
            builder.append(gameData.getGameName());
            builder.append(":\t");
            builder.append(gameData.getGameID());
            builder.append("\n");
        }

        return builder.toString();
    }

    public String playGame(String... params) throws ClientException {

        if (params.length >= 2) {

            int gameID = 0;
            try {
                gameID = Integer.parseInt(params[0]);

            }catch (NumberFormatException n) {
                throw new ClientException("Error: Invalid gameID: " + n.getMessage());
            }

            String color = params[1].toLowerCase().trim();
            ChessGame.TeamColor teamColor = color.equals("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            server.joinGame(authToken, new JoinGameRequest(teamColor, gameID));
            new GameplayClient(server, gameID, teamColor).run();

            return String.format("Game %s complete.", gameID);
        }

        throw new ClientException("Error: Expected: <gameID> [WHITE|BLACK]");
    }

    public String observeGame(String... params) throws ClientException {

        if (params.length >= 1) {

            int gameID = 0;
            try {
                gameID = Integer.parseInt(params[0]);

            }catch (NumberFormatException n) {
                throw new ClientException("Error: Invalid gameID: " + n.getMessage());
            }

            return "Game observation not currently supported";
        }

        throw new ClientException("Error: Expected: <gameID>");
    }

    public String help() {
        return String.format(
                """
                %s- create <NAME> %s- a game
                %s- list %s- games
                %s- join <ID> [WHITE|BLACK] %s- a game
                %s- observe <ID> %s- a game
                %s- logout %s- when you are done
                %s- quit %s- playing chess
                %s- help %s- with possible commands
                """,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA
        );
    }

    private String authToken = null;
    private String username = null;
}