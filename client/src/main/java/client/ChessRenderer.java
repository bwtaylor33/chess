package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessRenderer {

    public ChessRenderer(ChessBoard board, ChessGame.TeamColor perspective) {
        this.board = board;
        this.asWhite = perspective == ChessGame.TeamColor.WHITE;
    }

    public void display() {

        drawBoardBackground();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {

                ChessPiece piece = board.getPiece(new ChessPosition(row, col));

                if (piece != null) {
                    displayPiece(row, col, piece);
                }
            }
        }
    }

    private void drawBoardBackground() {

        System.out.print(ERASE_SCREEN);

        for (int row = 1; row <= 8; row++) {

            int pRow = asWhite ? row : 9 - row;

            for (int col = 1; col <= 8; col++) {

                System.out.print(moveCursorToLocation(ORIGIN_ROW + pRow, (ORIGIN_COL + col) * 3));
                System.out.print(getBackgroundColorForSquare(pRow, col));
                System.out.print("   ");
                System.out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
                // System.out.print("\u001B[0m");
            }
        }

        System.out.print(moveCursorToLocation(ORIGIN_ROW + 10, 0));

        System.out.flush();
    }

    private void displayPiece(int row, int col, ChessPiece piece) {

        int pRow = asWhite ? row : 9 - row;

        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print(moveCursorToLocation(ORIGIN_ROW + pRow, (ORIGIN_COL + col) * 3));
        System.out.print(getBackgroundColorForSquare(pRow, col));
        System.out.print(piece);
        System.out.print("\u001B[0m");
//        System.out.print(RESET_BG_COLOR + RESET_TEXT_COLOR);
        System.out.print(moveCursorToLocation(ORIGIN_ROW + 10, 0));

        System.out.flush();
    }

    private String getBackgroundColorForSquare(int row, int col){
        return (row + col) % 2 == 0 ? SET_BG_COLOR_DARK_GREY : SET_BG_COLOR_LIGHT_GREY;
    }

    private final ChessBoard board;
    private final boolean asWhite;

    private static final int ORIGIN_ROW = 1;
    private static final int ORIGIN_COL = 1;
}
