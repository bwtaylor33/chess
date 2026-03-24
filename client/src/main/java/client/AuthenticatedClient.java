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
                case "create game" -> createGame(params);
                case "list games" -> listGames();
                case "play game" -> playGame();
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

        if (params.length >= 1) {
            String gameName = params[0];
            CreateGameResult createGameResult = server.createGame(authToken, new CreateGameRequest(gameName));
            return String.format("Game %s has been created.", gameName);
        }
        throw new ResponseException("Error: Expected: <gameName>");
    }

    public String listGames() throws ResponseException {
        ListGamesResult listGamesResult = server.listGames(authToken);
        StringBuilder builder = new StringBuilder("Games:\n");
        for (GameData gameData : listGamesResult.games()) {
            builder.append("\t");
            builder.append(gameData.getGameName());
            builder.append(": ");
            builder.append(gameData.getGameID());
        }
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
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    private String authToken = null;
    private String username = null;
}