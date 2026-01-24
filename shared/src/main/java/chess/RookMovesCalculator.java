package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        // up
        moves.addAll(getSlideMoves(board, myPosition, 1, 0));

        // down
        moves.addAll(getSlideMoves(board, myPosition, -1, 0));

        // left
        moves.addAll(getSlideMoves(board, myPosition, 0, -1));

        // right
        moves.addAll(getSlideMoves(board, myPosition, 0, 1));

        return moves;
    }
}
