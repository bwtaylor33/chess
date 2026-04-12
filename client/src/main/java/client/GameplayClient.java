package client;

import java.util.Arrays;

import static ui.EscapeSequences.*;
import chess.ChessBoard;
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
                %s- move <FROM_ROW><FROM_COL> <TO_ROW><TO_COL> [PROMOTION_PIECE]%s - piece from and to coordinates (use PROMOTION_PIECE only if an eligible pawn)
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
        System.out.print(SET_TEXT_COLOR_MAGENTA);
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
        System.out.print(SET_TEXT_COLOR_RED);
        errorMessage.display();
        System.out.print(RESET_TEXT_COLOR);
        printPrompt();
    }

    private String movePiece(String... params) throws ClientException {

        if (params.length < 2){
            throw new ClientException("Error: Expected <fromRow><fromCol> <toRow><toCol>");
        }

        String from = params[0].trim().toLowerCase();
        String to = params[1].trim().toLowerCase();

        if(from.length() != 2 || to.length() != 2){
            throw new ClientException("Error: Expected to and from positions, defined a letter-digit combos (e.g. a1)");
        }

        int fromRow = from.charAt(0) - 'a' + 1;
        int fromCol = from.charAt(1) - '1' + 1;
        int toRow = to.charAt(0) - 'a' + 1;
        int toCol = to.charAt(1) - '1' + 1;

        if(fromRow < 1 || fromRow > 8 || fromCol < 1 || fromCol > 8 || toRow < 1 || toRow > 8 || toCol < 1 || toCol > 8){
            throw new ClientException("Error: Expected rows in range a-h and columns in range 1-8)");
        }

        ChessPiece.PieceType promotionPiece = null;

        // Handle promotion piece
        if(params.length >= 3){
            promotionPiece = mapPromotionPieceRequest(params[2].trim().toLowerCase());
        }

        ChessMove chessMove = new ChessMove(new ChessPosition(fromRow, fromCol), new ChessPosition(toRow, toCol), promotionPiece);
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
            throw new ClientException("Error: Expected: <row><col>");
        }

        String position = params[0].trim().toLowerCase();
        if(position.length() != 2){
            throw new ClientException("Error: Expected position as a letter-digit, 2-character combo (e.g. \"a1\"");
        }

        int row = position.charAt(0) - 'a' + 1;
        int col = position.charAt(1) - '1' + 1;

        if(row < 1 || row > 8 || col < 1 || col > 8){
            throw new ClientException("Error: Expected a row in range a-h and a column in range 1-8)");
        }

        // TODO: Show valid moves

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
        renderer.display();
    }

    private final String authToken;
    private final String username;
    private final int gameID;
    private final ChessGame.TeamColor color;
    private ChessGame game = new ChessGame();
    private boolean boardRefreshRequested = false;
}