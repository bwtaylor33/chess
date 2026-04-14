package client;

import java.util.Arrays;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResult;
import model.response.RegisterResult;

import static ui.EscapeSequences.*;

public class UnauthenticatedClient extends BaseClient {

    public UnauthenticatedClient(ServerFacade serverFacade) {
        super(serverFacade, String.format("\n%s%s Welcome to the Chess client. Sign in to get started!", SET_TEXT_COLOR_WHITE, WHITE_QUEEN));
    }

    public void printPrompt() {
        System.out.printf("%s[LOGGED_OUT] >>> %s", SET_TEXT_COLOR_LIGHT_GREY, SET_TEXT_COLOR_GREEN);
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

        } catch (ClientException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ClientException {

        if (params.length != 2) {
            throw new ClientException("Error: Expected: <username> <password>");
        }

        String username = params[0];
        String password = params[1];

        LoginResult loginResult = server.login(new LoginRequest(username, password));
        new AuthenticatedClient(server, loginResult.authToken(), loginResult.username()).run();

        return String.format("%s is now signed out.", username);
    }

    public String register(String... params) throws ClientException {

        if (params.length != 3) {
            throw new ClientException("Error: Expected: <username> <password> <email>");
        }

        String username = params[0];
        String password = params[1];
        String email = params[2];

        RegisterResult registerResult = server.register(new RegisterRequest(username, password, email));
        new AuthenticatedClient(server, registerResult.authToken(), registerResult.username()).run();

        return String.format("%s is now signed out.", username);
    }

    public String help() {
        return String.format(
                """
                %s- register <USERNAME> <PASSWORD> <EMAIL> %s- to create an account
                %s- login <USERNAME> <PASSWORD> %s- to play chess
                %s- quit %s- playing chess
                %s- help %s- with possible commands
                """,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA
        );
    }
}