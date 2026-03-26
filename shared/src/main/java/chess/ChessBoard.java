package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static ui.EscapeSequences.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                board[row][column] = null;
            }
        }

        // add all pieces to initial positions
        addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1,6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPawns(ChessGame.TeamColor.WHITE);

        addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPawns(ChessGame.TeamColor.BLACK);
    }

    public void display(boolean viewAsWhite) {

        List<List<String>> boardRows = new ArrayList<>();
        List<String> row = new ArrayList<>();

        row.add(RESET_TEXT_COLOR);
        row.add(SET_TEXT_COLOR_BLACK);
        boardRows.add(row);

        row = new ArrayList<>();
        row.add(RESET_BG_COLOR);
        row.add(SET_BG_COLOR_DARK_GREEN);
        row.add("   ");
        row.add(" a ");
        row.add(" b ");
        row.add(" c ");
        row.add(" d ");
        row.add(" e ");
        row.add(" f ");
        row.add(" g ");
        row.add(" h ");
        row.add("   ");
        row.add(SET_BG_COLOR_DARK_GREEN);
        row.add(RESET_BG_COLOR);
        boardRows.add(row);

//
//        boardRows.add(String.format("%s%s 8 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 8 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_WHITE,
//                BLACK_ROOK,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_KNIGHT,
//                SET_BG_COLOR_WHITE,
//                BLACK_BISHOP,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_QUEEN,
//                SET_BG_COLOR_WHITE,
//                BLACK_KING,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_BISHOP,
//                SET_BG_COLOR_WHITE,
//                BLACK_KNIGHT,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_ROOK,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s 7 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 7 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_PAWN,
//                SET_BG_COLOR_WHITE,
//                BLACK_PAWN,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_PAWN,
//                SET_BG_COLOR_WHITE,
//                BLACK_PAWN,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_PAWN,
//                SET_BG_COLOR_WHITE,
//                BLACK_PAWN,
//                SET_BG_COLOR_LIGHT_GREY,
//                BLACK_PAWN,
//                SET_BG_COLOR_WHITE,
//                BLACK_PAWN,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s 6 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 6 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s 5 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 5 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s 4 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 4 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s 3 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 3 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_LIGHT_GREY,
//                EMPTY,
//                SET_BG_COLOR_WHITE,
//                EMPTY,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s 2 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 2 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_WHITE,
//                WHITE_PAWN,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_PAWN,
//                SET_BG_COLOR_WHITE,
//                WHITE_PAWN,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_PAWN,
//                SET_BG_COLOR_WHITE,
//                WHITE_PAWN,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_PAWN,
//                SET_BG_COLOR_WHITE,
//                WHITE_PAWN,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_PAWN,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s 1 %s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s 1 %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_ROOK,
//                SET_BG_COLOR_WHITE,
//                WHITE_KNIGHT,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_BISHOP,
//                SET_BG_COLOR_WHITE,
//                WHITE_QUEEN,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_KING,
//                SET_BG_COLOR_WHITE,
//                WHITE_BISHOP,
//                SET_BG_COLOR_LIGHT_GREY,
//                WHITE_KNIGHT,
//                SET_BG_COLOR_WHITE,
//                WHITE_ROOK,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(String.format("%s%s    a  b  c  d  e  f  g  h    %s%s",
//                RESET_BG_COLOR,
//                SET_BG_COLOR_DARK_GREEN,
//                SET_BG_COLOR_DARK_GREEN,
//                RESET_BG_COLOR
//        ));
//
//        boardRows.add(SET_TEXT_COLOR_BLACK + RESET_TEXT_COLOR);
//
        if (!viewAsWhite) {

            List<List<String>> reversedBoard = boardRows.reversed();
            for (int i = 0; i < reversedBoard.size(); i++) {

                List<String> r = reversedBoard.get(i);
                List<String> reverseRow = r.reversed();
                reversedBoard.set(i, reverseRow);
            }
            boardRows = reversedBoard;
        }

        for (List<String> r: boardRows) {
            for (String item: r) {
                System.out.print(item);
            }
            System.out.println();
        }
    }

    private void addPawns(ChessGame.TeamColor color) {

        int row = color == ChessGame.TeamColor.WHITE ? 2 : 7;

        for (int column = 1; column <= 8; column++) {
            addPiece(new ChessPosition(row, column), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }

    private final ChessPiece[][] board = new ChessPiece[8][8];
}
