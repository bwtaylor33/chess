package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        // up
        ChessPosition up = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if (isInBoundsAndNotBlocked(board, up)) {
            moves.add(new ChessMove(myPosition, up, null));
        }

        // down
        ChessPosition down = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        if (isInBoundsAndNotBlocked(board, down)) {
            moves.add(new ChessMove(myPosition, down, null));
        }

        // left
        ChessPosition left = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        if (isInBoundsAndNotBlocked(board, left)) {
            moves.add(new ChessMove(myPosition, left, null));
        }

        // right
        ChessPosition right = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        if (isInBoundsAndNotBlocked(board, right)) {
            moves.add(new ChessMove(myPosition, right, null));
        }

        // diagonal up-left
        ChessPosition upLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (isInBoundsAndNotBlocked(board, upLeft)) {
            moves.add(new ChessMove(myPosition, upLeft, null));
        }

        // diagonal up-right
        ChessPosition upRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if (isInBoundsAndNotBlocked(board, upRight)) {
            moves.add(new ChessMove(myPosition, upRight, null));
        }

        // diagonal down-left
        ChessPosition downLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if (isInBoundsAndNotBlocked(board, downLeft)) {
            moves.add(new ChessMove(myPosition, downLeft, null));
        }

        // diagonal down-right
        ChessPosition downRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if (isInBoundsAndNotBlocked(board, downRight)) {
            moves.add(new ChessMove(myPosition, downRight, null));
        }

        return moves;
    }
}
