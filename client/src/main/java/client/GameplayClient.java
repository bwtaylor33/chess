package client;

import java.util.Arrays;

import chess.ChessBoard;
import chess.ChessGame;
import service.ResponseException;
import static ui.EscapeSequences.*;

public class GameplayClient extends BaseClient {

    public GameplayClient(ServerFacade serverFacade, int gameID, ChessGame.TeamColor color) throws ResponseException {
        super(serverFacade, "Good luck!");
        this.gameID = gameID;
        this.color = color;
    }

    public void printPrompt() {
        System.out.printf("\n%s[LOGGED_IN to game %d] >>> %s", SET_TEXT_COLOR_LIGHT_GREY, gameID, SET_TEXT_COLOR_GREEN);
        drawGameBoard();
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            // removes the temporary exception
            if (params.length == 99) {
                throw new ResponseException("hack");
            }

            return switch (cmd) {
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return String.format(
                """
                %s- quit %s- playing chess
                %s- help %s- with possible commands
                """,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA
        );
    }

    private void drawGameBoard() {
        new ChessBoard().display();
    }

    private final int gameID;
    private final ChessGame.TeamColor color;
}