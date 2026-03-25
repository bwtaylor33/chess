package client;

import chess.ChessGame;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;
import service.ResponseException;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class AuthenticatedClient extends BaseClient {

    public AuthenticatedClient(ServerFacade server, String authToken, String username) throws ResponseException {
        super(server);
        this.authToken = authToken;
        this.username = username;
    }

    public void printPrompt() {
        System.out.printf("\n[LOGGED_IN as %s] >>> %s", username, SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame();
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws ResponseException {
        server.logout(username);
        return "quit";
    }

    public String createGame(String... params) throws ResponseException {
        System.out.println("In createGame()");

        if (params.length >= 1) {
            String gameName = params[0];
            System.out.println("Creating gameName: " + gameName);
            CreateGameResult createGameResult = server.createGame(authToken, new CreateGameRequest(gameName));
            System.out.println("Returning");
            return String.format("Game %s has been created with ID: %d.", gameName, createGameResult.gameID());
        }
        throw new ResponseException("Error: Expected: <gameName>");
    }

    public String listGames() throws ResponseException {
        System.out.println("Entering listGames()");
        ListGamesResult listGamesResult = server.listGames(authToken);
        System.out.println("Got gamesResult");
        StringBuilder builder = new StringBuilder("Games:\n");
        for (GameData gameData : listGamesResult.games()) {
            builder.append("\t");
            builder.append(gameData.getGameName());
            builder.append(": ");
            builder.append(gameData.getGameID());
        }

        System.out.println("List = " + builder.toString());
        return builder.toString();
    }

    public String playGame(String... params) throws ResponseException {
        int gameID = 0;
        try {
            gameID = Integer.parseInt(params[0]);

        }catch (NumberFormatException n) {
            throw new ResponseException("Error: Invalid gameID: " + n.getMessage());
        }

        String color = params[1].toLowerCase().trim();
        ChessGame.TeamColor teamColor = color.equals("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        server.joinGame(authToken, new JoinGameRequest(teamColor, gameID));
        new GameplayClient(server, gameID, teamColor);
        return String.format("Game %s complete.", gameID);
    }

    public String help() {
        return """
                - create <NAME> - a game
                - list - games
                - join <ID> - [WHITE|BLACK] - a game
                - observe <ID> - a game
                - logout - when you are done
                - quit - playing chess
                - help - with possible commands
                """;
    }

    private String authToken = null;
    private String username = null;
}