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

    protected boolean isInBoundsAndNotBlocked(ChessBoard board, ChessPosition position) {
        if (position.getRow() < 1 || position.getRow() > 8) {
            return false;
        }
        if (position.getColumn() < 1 || position.getColumn() > 8) {
            return false;
        }
        if (board.getPiece(position) != null && board.getPiece(position).getTeamColor() == color) {
            return false;
        }
        return true;
    }

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

    public static PieceMovesCalculator getCalculator(ChessPiece piece) {
        PieceMovesCalculator pieceMovesCalculator = null;
        switch(piece.getPieceType()) {
            case KING:
                pieceMovesCalculator = new KingMovesCalculator(piece.getTeamColor());
                break;
            case QUEEN:
                pieceMovesCalculator = new QueenMovesCalculator(piece.getTeamColor());
                break;
            case BISHOP:
                pieceMovesCalculator = new BishopMovesCalculator(piece.getTeamColor());
                break;
            case KNIGHT:
                pieceMovesCalculator = new KnightMovesCalculator(piece.getTeamColor());
                break;
            case ROOK:
                pieceMovesCalculator = new RookMovesCalculator(piece.getTeamColor());
                break;
            case PAWN:
                pieceMovesCalculator = new PawnMovesCalculator(piece.getTeamColor());
                break;
        }
        return pieceMovesCalculator;
    }

    private ChessGame.TeamColor color;

}
