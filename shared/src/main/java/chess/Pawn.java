package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends ChessPiece {
    public Pawn(ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.PAWN);
    }

    protected boolean isInvalid(ChessBoard board, ChessMove move) {
        return true;
    }

    protected Collection<ChessMove> possibleMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        // attack left
        if (myPosition.getColumn() > 1) {
            int deltaRow = getTeamColor() == ChessGame.TeamColor.WHITE ? -1 : 1;
            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + deltaRow,myPosition.getColumn() - 1), null);
        }

        // attack right
        if (myPosition.getColumn() <= 7) {
            int deltaRow = getTeamColor() == ChessGame.TeamColor.WHITE ? -1 : 1;
            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + deltaRow, myPosition.getColumn() + 1), null);
        }

        // forward one
        int deltaRow = getTeamColor() == ChessGame.TeamColor.WHITE ? -1 : 1;
        possibleMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + deltaRow, myPosition.getColumn()), null);

        // opening move
        if ((getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
            int newRow = getTeamColor() == ChessGame.TeamColor.WHITE ? 4 : 5;
            possibleMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, myPosition.getColumn()), null));
        }

        return possibleMoves;
    }

}
