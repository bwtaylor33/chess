package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {

    public LoadGameMessage(String username, ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.username = username;
        this.game = game;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame getGame() {
        return game;
    }

    private final String username;
    private final ChessGame game;
}
