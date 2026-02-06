package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        board.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(getBoard(), chessGame.getBoard()) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), turn);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        // check if empty space on board
        if (piece == null) {
            return null;
        }

        // place valid moves into this array
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        // iterate through all possible moves
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : possibleMoves) {
            if (isValidMove(move)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece startPiece = board.getPiece(move.getStartPosition());

        // checking if piece exists at start position
        if (startPiece == null) {
            throw new InvalidMoveException("no piece exists at designated start position");
        }

        // make sure you are not moving out of turn
        if (startPiece.getTeamColor() != turn) {
            throw new InvalidMoveException("attempted to move out of turn");
        }

        // move is invalid or results in check
        if (!isValidMove(move)) {
            throw new InvalidMoveException("move is invalid or results in check");
        }

        // handling promotions
        ChessPiece piece = move.getPromotionPiece() == null ? startPiece : new ChessPiece(startPiece.getTeamColor(), move.getPromotionPiece());

        // moving piece on the board
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
        toggleTeamTurn();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    System.out.println("checking moves for " + piece + " " + piece.getTeamColor() + " at " + position);

                    Collection<ChessMove> validMoves = piece.pieceMoves(board, position);

                    for (ChessMove move : validMoves) {
                        ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                        if (capturedPiece != null && capturedPiece.getPieceType() == ChessPiece.PieceType.KING) {
                            System.out.println("is in check from " + piece + " @ " + position);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int i=1; i<=8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    System.out.println("checking moves for " + piece + " " + piece.getTeamColor() + " at " + position);

                    Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (int i=1; i<=8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != teamColor) {
                    System.out.println("checking moves for " + piece + " " + piece.getTeamColor() + " at " + position);

                    Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private boolean isValidMove(ChessMove move) {
        ChessPiece startPiece = board.getPiece(move.getStartPosition());

        if (startPiece == null) {
            return false;
        }

        // first check if move is even possible
        if (!startPiece.pieceMoves(board, move.getStartPosition()).contains(move)) {
            return false;
        }

        // create a board that represents doing this potential move
        ChessPiece capturedPiece = board.getPiece(move.getEndPosition());

        board.addPiece(move.getEndPosition(), startPiece);
        board.addPiece(move.getStartPosition(), null);
        toggleTeamTurn();

        // test to make sure that the move doesn't put himself into check
        boolean isInCheck = isInCheck(startPiece.getTeamColor());

        // now put board back to original state
        board.addPiece(move.getStartPosition(), startPiece);
        board.addPiece(move.getEndPosition(), capturedPiece);
        toggleTeamTurn();

        System.out.println(move.toString() + " " + isInCheck);
        return !isInCheck;
    }

    private void toggleTeamTurn() {
        turn = turn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
    }

    private ChessBoard board = new ChessBoard();
    private TeamColor turn = TeamColor.WHITE;
}
