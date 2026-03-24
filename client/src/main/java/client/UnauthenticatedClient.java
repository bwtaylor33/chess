package client;

import java.util.Arrays;
import service.ResponseException;
import server.ServerFacade;
import static ui.EscapeSequences.*;

public class UnauthenticatedClient extends BaseClient {

    public UnauthenticatedClient(ServerFacade serverFacade) throws ResponseException {
        super(serverFacade);
    }

    public void printPrompt() {
        System.out.print("\n[LOGGED_OUT] >>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            String username = params[0];
            String password = params[1];
            server.login(username, password);
            new AuthenticatedClient.run(server, username);
            return String.format("%s is now signed out.", username);
        }
        throw new ResponseException("Error: Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            server.register(username, password, email);
            new AuthenticatedClient.run(server, username);
            return String.format("%s is now signed out.", username);
        }
        throw new ResponseException("Error: Expected: <username> <password> <email>");
    }

    public String help() {
        return """
               - login <username> <password>
               - register <username> <password> <email>
               - quit
               """;
    }
}