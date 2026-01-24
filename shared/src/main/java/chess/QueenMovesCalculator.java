package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // queen has same moves as bishop + rook

        // bishop moves
        Collection<ChessMove> moves = new BishopMovesCalculator(getColor()).pieceMoves(board, myPosition);

        // rook moves
        Collection<ChessMove> rookMoves = new RookMovesCalculator(getColor()).pieceMoves(board, myPosition);

        moves.addAll(rookMoves);

        return moves;
    }
}
