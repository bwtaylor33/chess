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

        String s = "";

        if (pieceType == PieceType.KING) {
            s = "k";

        } else if (pieceType == PieceType.QUEEN) {
            s = "q";

        } else if (pieceType == PieceType.ROOK) {
            s = "r";

        } else if (pieceType == PieceType.KNIGHT) {
            s = "k";

        } else if (pieceType == PieceType.BISHOP) {
            s = "b";

        } else if (pieceType == PieceType.PAWN) {
            s = "p";
        }

        if (pieceColor == ChessGame.TeamColor.BLACK) {
            s = s.toUpperCase();
        }

        return s;
    }

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;
}
