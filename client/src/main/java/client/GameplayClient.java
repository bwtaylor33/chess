package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static ui.EscapeSequences.*;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessMove;
import chess.ChessPosition;
import websocket.commands.*;
import websocket.messages.*;

public class GameplayClient extends BaseClient implements ServerMessageConsumer {

    public GameplayClient(ServerFacade server, String authToken, String username, int gameID, ChessGame.TeamColor color) throws ClientException {

        super(server, String.format("%s%s Good luck, %s!", SET_TEXT_COLOR_WHITE, WHITE_QUEEN, username));

        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
        this.color = color;

        server.setServerMessageConsumer(this);

        server.sendCommand(new ConnectCommand(authToken, gameID));
    }

    public void printPrompt() {

        if(boardRefreshRequested){
            drawGameBoard();
            boardRefreshRequested = false;
        }

        String promptStub = color == null ? "%s[OBSERVING game %d] >>> %s" : "%s[PLAYING game %d] >>> %s";
        System.out.printf(promptStub, SET_TEXT_COLOR_LIGHT_GREY, gameID, SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {

        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "move" -> movePiece(params);
                case "show" -> showValidMoves(params);
                case "leave" -> leaveGame();
                case "resign" -> resignGame();
                case "redraw" -> redrawBoard();
                case "quit" -> "quit";
                default -> help();
            };

        } catch (ClientException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return String.format(
                """
                %s- move <FROM_COL><FROM_ROW> <TO_COL><TO_ROW> [PROMOTION_PIECE]%s - for possible pawn promotion
                %s- show <ROW><COL>%s - valid moves for the piece at space
                %s- leave %s- a game
                %s- resign %s- resign the game
                %s- redraw %s- redraw the board
                %s- quit %s- playing chess
                %s- help %s- with possible commands
                """,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA,
                SET_TEXT_COLOR_BLUE, SET_TEXT_COLOR_MAGENTA
        );
    }

    public void notify(NotificationMessage notificationMessage) {
        System.out.println(SET_TEXT_COLOR_MAGENTA);
        notificationMessage.display();
        System.out.print(RESET_TEXT_COLOR);
        printPrompt();
    }

    public void loadGame(LoadGameMessage loadGameMessage) {
        game = loadGameMessage.getGame();
        boardRefreshRequested = true;
        printPrompt();
    }

    public void error(ErrorMessage errorMessage) {
        System.out.println(SET_TEXT_COLOR_RED);
        errorMessage.display();
        System.out.print(RESET_TEXT_COLOR);
        printPrompt();
    }

    private String movePiece(String... params) throws ClientException {

        if (params.length < 2){
            throw new ClientException("Error: Expected <fromCol><fromRow> <toCol><toRow>");
        }

        String from = params[0].trim();
        String to = params[1].trim();

        ChessPiece.PieceType promotionPiece = null;

        // Handle promotion piece
        if(params.length >= 3){
            promotionPiece = mapPromotionPieceRequest(params[2].trim().toLowerCase());
        }

        // Converts string to valid chess move
        ChessMove chessMove = null;
        try {
            chessMove = new ChessMove(ChessPosition.fromRowColumnString(from), ChessPosition.fromRowColumnString(to), promotionPiece);

        } catch (IllegalArgumentException i) {
            throw new ClientException(i.getMessage());
        }

        server.sendCommand(new MakeMoveCommand(authToken, gameID, chessMove));

        return "Move completed.";
    }

    private ChessPiece.PieceType mapPromotionPieceRequest(String pieceString) throws ClientException {

        return switch(pieceString){
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook"	-> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "pawn"	-> ChessPiece.PieceType.PAWN;
            default	-> throw new ClientException("Error: Piece type must be one of: QUEEN, ROOK, BISHOP, KNIGHT, PAWN");
        };
    }

    private String showValidMoves(String... params) throws ClientException {

        if (params.length < 1) {
            throw new ClientException("Error: Expected: <col><row>");
        }

        String position = params[0].trim();

        // Converts string to valid chess move
        ChessPosition chessPosition = null;
        try {
            chessPosition = ChessPosition.fromRowColumnString(position);

        } catch (IllegalArgumentException i) {
            throw new ClientException(i.getMessage());
        }

        Collection<ChessMove> validMoves = game.validMoves(chessPosition);
        if (validMoves == null) {
            throw new ClientException("Error: No piece available at specified coordinate.");
        }

        // Pull destination chess position for each valid move
        highlightedPositions.clear();
        for (ChessMove validMove : validMoves) {
            highlightedPositions.add(validMove.getEndPosition());
        }

        boardRefreshRequested = true;

        return String.format("Moves highlighted for piece at %s.", position);
    }

    private String leaveGame() throws ClientException {
        server.sendCommand(new LeaveGameCommand(authToken, gameID));
        return "quit";
    }

    private String resignGame() throws ClientException {
        server.sendCommand(new ResignGameCommand(authToken, gameID));
        return "quit";
    }

    private String redrawBoard() {
        drawGameBoard();
        return "";
    }

    private void drawGameBoard() {
        ChessRenderer renderer = new ChessRenderer(game.getBoard(), color);
        renderer.display(highlightedPositions);
        highlightedPositions.clear();
    }

    private final String authToken;
    private final String username;
    private final int gameID;
    private final ChessGame.TeamColor color;
    private ChessGame game = new ChessGame();
    private boolean boardRefreshRequested = false;
    private ArrayList<ChessPosition> highlightedPositions = new ArrayList<>();
}