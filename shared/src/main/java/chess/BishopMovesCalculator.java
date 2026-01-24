package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        // upLeft
        moves.addAll(getSlideMoves(board, myPosition, 1, -1));

        // upRight
        moves.addAll(getSlideMoves(board, myPosition, 1, 1));

        // downLeft
        moves.addAll(getSlideMoves(board, myPosition, -1, -1));

        // downRight
        moves.addAll(getSlideMoves(board, myPosition, -1, 1));

        return moves;
    }
}
