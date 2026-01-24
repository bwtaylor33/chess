package chess;

import java.util.Collection;

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
