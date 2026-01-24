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
