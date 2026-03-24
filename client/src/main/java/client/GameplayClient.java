package client;

import java.util.Arrays;

import chess.ChessGame;
import service.ResponseException;
import server.ServerFacade;
import static ui.EscapeSequences.*;

public class GameplayClient extends BaseClient {

    public GameplayClient(ServerFacade serverFacade, int gameID, ChessGame.TeamColor color) throws ResponseException {
        super(serverFacade);
        this.gameID = gameID;
        this.color = color;
    }

    public void printPrompt() {
        System.out.printf("\n[LOGGED_IN to game %d] >>> %s", gameID, SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
               - login <username> <password>
               - register <username> <password> <email>
               - quit
               """;
    }

    private final int gameID;
    private final ChessGame.TeamColor color;
}