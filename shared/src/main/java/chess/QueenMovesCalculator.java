package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

//        // forward one
//        int deltaRow = color == ChessGame.TeamColor.WHITE ? -1 : 1;
//        ChessPosition newSpot = new ChessPosition(myPosition.getRow() + deltaRow, myPosition.getColumn());
//        if (board.getPiece(newSpot) == null) {
//            moves.add(new ChessMove(myPosition, newSpot, null));
//        }
//
//        // attack left
//        if (myPosition.getColumn() > 1) {
//            deltaRow = color == ChessGame.TeamColor.WHITE ? -1 : 1;
//            newSpot = new ChessPosition(myPosition.getRow() + deltaRow,myPosition.getColumn() - 1);
//            if (board.getPiece(newSpot).getTeamColor() != getColor()) {
//                moves.add(new ChessMove(myPosition, newSpot, null));
//            }
//        }
//
//        // attack right
//        if (myPosition.getColumn() <= 7) {
//            deltaRow = color == ChessGame.TeamColor.WHITE ? -1 : 1;
//            newSpot = new ChessPosition(myPosition.getRow() + deltaRow, myPosition.getColumn() + 1);
//            moves.add(new ChessMove(myPosition, newSpot, null));
//        }
//
//        // opening move
//        if ((color == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (color == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
//            int newRow = color == ChessGame.TeamColor.WHITE ? 4 : 5;
//            int intermediateRow = color == ChessGame.TeamColor.WHITE ? 3 : 6;
//            newSpot = new ChessPosition(newRow, myPosition.getColumn());
//            ChessPosition intermediateSpot = new ChessPosition(intermediateRow, myPosition.getColumn());
//            if (board.getPiece(intermediateSpot) == null) {
//                moves.add(new ChessMove(myPosition, newSpot, null));
//            }
//        }

        return moves;
    }
}
