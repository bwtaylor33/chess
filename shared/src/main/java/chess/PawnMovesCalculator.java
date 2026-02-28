package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public PawnMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        int deltaRow = color == ChessGame.TeamColor.WHITE ? 1 : -1;

        // forward one
        ChessPosition newSpot = new ChessPosition(myPosition.getRow() + deltaRow, myPosition.getColumn());

        if (isInBoundsAndNotBlocked(board, newSpot)) {
            if (board.getPiece(newSpot) == null) {
                moves.addAll(getMovesCheckingForPromotion(myPosition, color, newSpot));
            }
        }

        // attack left
        if (myPosition.getColumn() > 1) {

            newSpot = new ChessPosition(myPosition.getRow() + deltaRow,myPosition.getColumn() - 1);

            if (isInBoundsAndNotBlocked(board, newSpot)) {
                if (board.getPiece(newSpot) != null && board.getPiece(newSpot).getTeamColor() != getColor()) {
                    moves.addAll(getMovesCheckingForPromotion(myPosition, color, newSpot));
                }
            }
        }

        // attack right
        if (myPosition.getColumn() <= 7) {

            newSpot = new ChessPosition(myPosition.getRow() + deltaRow, myPosition.getColumn() + 1);

            if (isInBoundsAndNotBlocked(board, newSpot)) {
                if (board.getPiece(newSpot) != null && board.getPiece(newSpot).getTeamColor() != getColor()) {
                    moves.addAll(getMovesCheckingForPromotion(myPosition, color, newSpot));
                }
            }
        }

        // opening move
        if ((color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {

            int newRow = color == ChessGame.TeamColor.WHITE ? 4 : 5;
            int intermediateRow = color == ChessGame.TeamColor.WHITE ? 3 : 6;

            newSpot = new ChessPosition(newRow, myPosition.getColumn());
            ChessPosition intermediateSpot = new ChessPosition(intermediateRow, myPosition.getColumn());

            if (board.getPiece(intermediateSpot) == null && board.getPiece(newSpot) == null) {
                moves.add(new ChessMove(myPosition, newSpot, null));
            }
        }

        return moves;
    }

    private ArrayList<ChessMove> getMovesCheckingForPromotion(ChessPosition myPosition, ChessGame.TeamColor color, ChessPosition newSpot) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        ArrayList<ChessMove> promotionMoves = getPromotionMoves(myPosition, color, newSpot);

        if (promotionMoves.isEmpty()) {
            moves.add(new ChessMove(myPosition, newSpot, null));

        } else {
            moves.addAll(getPromotionMoves(myPosition, color, newSpot));
        }

        return moves;
    }

    private ArrayList<ChessMove> getPromotionMoves(ChessPosition myPosition, ChessGame.TeamColor color, ChessPosition newSpot) {

        ArrayList<ChessMove> moves = new ArrayList<>();

        if ((newSpot.getRow() == 1 && color == ChessGame.TeamColor.BLACK) ||
                (newSpot.getRow() == 8 && color == ChessGame.TeamColor.WHITE)) {

            moves.add(new ChessMove(myPosition, newSpot, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(myPosition, newSpot, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(myPosition, newSpot, ChessPiece.PieceType.KNIGHT));
            moves.add(new ChessMove(myPosition, newSpot, ChessPiece.PieceType.ROOK));
        }

        return moves;
    }
}
