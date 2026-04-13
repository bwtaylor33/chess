package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {

        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPiece that = (ChessPiece) o;

        return pieceColor == that.pieceColor && getPieceType() == that.getPieceType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, getPieceType());
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        PieceMovesCalculator movesCalculator = PieceMovesCalculator.getCalculator(board.getPiece(myPosition));

        return movesCalculator.pieceMoves(board, myPosition);
    }

    @Override
    public String toString() {

        return switch (pieceType) {
            case PieceType.PAWN -> BLACK_PAWN;
            case PieceType.ROOK -> BLACK_ROOK;
            case PieceType.KNIGHT -> BLACK_KNIGHT;
            case PieceType.BISHOP -> BLACK_BISHOP;
            case PieceType.KING -> BLACK_KING;
            default -> BLACK_QUEEN;
        };
    }

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;

    public static final String BLACK_KING = " ♚ ";
    public static final String BLACK_QUEEN = " ♛ ";
    public static final String BLACK_BISHOP = " ♝ ";
    public static final String BLACK_KNIGHT = " ♞ ";
    public static final String BLACK_ROOK = " ♜ ";
    public static final String BLACK_PAWN = " ♟ ";
}
