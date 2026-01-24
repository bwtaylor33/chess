package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        Collection<ChessMove> moves = new BishopMovesCalculator(color).pieceMoves(board, myPosition);

        Collection<ChessMove> rookMoves = new RookMovesCalculator(color).pieceMoves(board, myPosition);

        moves.addAll(rookMoves);

        return moves;
    }
}
