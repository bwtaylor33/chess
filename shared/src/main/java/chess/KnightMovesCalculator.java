package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        // up 1, right 2
        ChessPosition up1right2 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
        if (isInBoundsAndNotBlocked(board, up1right2)) {
            moves.add(new ChessMove(myPosition, up1right2, null));
        }

        // up 1, left 2
        ChessPosition up1left2 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
        if (isInBoundsAndNotBlocked(board, up1left2)) {
            moves.add(new ChessMove(myPosition, up1left2, null));
        }

        // up 2, right 1
        ChessPosition up2right1 = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
        if (isInBoundsAndNotBlocked(board, up2right1)) {
            moves.add(new ChessMove(myPosition, up2right1, null));
        }

        // up 2, left 1
        ChessPosition up2left1 = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
        if (isInBoundsAndNotBlocked(board, up2left1)) {
            moves.add(new ChessMove(myPosition, up2left1, null));
        }

        // down 1, right 2
        ChessPosition down1right2 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
        if (isInBoundsAndNotBlocked(board, down1right2)) {
            moves.add(new ChessMove(myPosition, down1right2, null));
        }

        // down 1, left 2
        ChessPosition down1left2 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
        if (isInBoundsAndNotBlocked(board, down1left2)) {
            moves.add(new ChessMove(myPosition, down1left2, null));
        }

        // down 2, right 1
        ChessPosition down2right1 = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
        if (isInBoundsAndNotBlocked(board, down2right1)) {
            moves.add(new ChessMove(myPosition, down2right1, null));
        }

        // down 2, left 1
        ChessPosition down2left1 = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
        if (isInBoundsAndNotBlocked(board, down2left1)) {
            moves.add(new ChessMove(myPosition, down2left1, null));
        }

        return moves;
    }
}
