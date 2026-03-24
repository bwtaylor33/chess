package client;

import server.ServerFacade;
import service.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class AuthenticatedClient extends BaseClient {

    public AuthenticatedClient(ServerFacade server, String username) throws ResponseException {
        super(server);
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
            server.createGame(gameName);
            return String.format("Game %s has been created.", gameName);
        }
        throw new ResponseException("Error: Expected: <gameName>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        server.listGames();
        return String.format("%s left the shop", username);
    }

    public String playGame(String... params) throws ResponseException {
        assertSignedIn();
        int gameID = Integer.parseInt(params[0]);
        String color = params[1];
        server.joinGame(gameID, color);
        new GameplayClient(server, gameID, color);
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

    private String username = null;
}