package client;

import java.util.Arrays;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResult;
import model.response.RegisterResult;
import service.ResponseException;

import static ui.EscapeSequences.*;

public class UnauthenticatedClient extends BaseClient {

    public UnauthenticatedClient(ServerFacade serverFacade) {
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
            LoginResult loginResult = server.login(new LoginRequest(username, password));
            new AuthenticatedClient(server, loginResult.authToken(), loginResult.username()).run();
            return String.format("%s is now signed out.", username);
        }
        throw new ResponseException("Error: Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResult registerResult = server.register(new RegisterRequest(username, password, email));
            new AuthenticatedClient(server, registerResult.authToken(), registerResult.username()).run();
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