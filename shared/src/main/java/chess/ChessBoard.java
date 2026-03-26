package chess;

import java.util.Arrays;
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

    public void display() {
        displayEmptyBoard();
    }

    private void displayEmptyBoard() {
        System.out.println(String.format(
                """
                %s%s
                %s    a  b  c  d  e  f  g  h   %s
                %s 8                           8 %s
                %s 7                           7 %s
                %s 6                           6 %s
                %s 5                           5 %s
                %s 4                           4 %s
                %s 3                           3 %s
                %s 2                           2 %s
                %s 1                           1 %s
                %s    a  b  c  d  e  f  g  h    %s
                """,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY,
                SET_BG_COLOR_DARK_GREEN, SET_TEXT_COLOR_DARK_GREY
                ));
    }

    private void addPawns(ChessGame.TeamColor color) {

        int row = color == ChessGame.TeamColor.WHITE ? 2 : 7;

        for (int column = 1; column <= 8; column++) {
            addPiece(new ChessPosition(row, column), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }

    private final ChessPiece[][] board = new ChessPiece[8][8];
}
