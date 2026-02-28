package chess;

import java.util.Collection;
import java.util.ArrayList;

abstract public class PieceMovesCalculator {

    abstract public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    protected PieceMovesCalculator(ChessGame.TeamColor color) {
        this.color = color;
    }

    protected ChessGame.TeamColor getColor() {
        return color;
    }

    // check to see if move will be in bounds and not blocked by piece on same team
    protected boolean isInBoundsAndNotBlocked(ChessBoard board, ChessPosition position) {

        if (position.getRow() < 1 || position.getRow() > 8) {
            return false;
        }

        if (position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }

        return board.getPiece(position) == null || board.getPiece(position).getTeamColor() != color;
    }

    // used for rook and bishop moves. repeats same move until it is an invalid move
    protected ArrayList<ChessMove> getSlideMoves(ChessBoard board, ChessPosition position, int deltaRow, int deltaColumn) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();

        while (true) {

            row += deltaRow;
            column += deltaColumn;
            ChessPosition testPosition = new ChessPosition(row, column);

            if (isInBoundsAndNotBlocked(board, testPosition)) {

                moves.add(new ChessMove(position, testPosition, null));

                if (board.getPiece(testPosition) != null) {
                    // hit an opponent
                    break;
                }
            } else {
                // ran into own piece or out of bounds
                break;
            }
        }

        return moves;
    }

    // factory returns move calculator for a given piece
    public static PieceMovesCalculator getCalculator(ChessPiece piece) {

        return switch (piece.getPieceType()) {
            case KING -> new KingMovesCalculator(piece.getTeamColor());
            case QUEEN -> new QueenMovesCalculator(piece.getTeamColor());
            case BISHOP -> new BishopMovesCalculator(piece.getTeamColor());
            case KNIGHT -> new KnightMovesCalculator(piece.getTeamColor());
            case ROOK -> new RookMovesCalculator(piece.getTeamColor());
            case PAWN -> new PawnMovesCalculator(piece.getTeamColor());
        };
    }

    private final ChessGame.TeamColor color;
}
