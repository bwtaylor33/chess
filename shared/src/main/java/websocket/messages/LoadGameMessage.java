package websocket.messages;

public class LoadGameMessage extends ServerMessage {

    public LoadGameMessage(String username, int gameID) {
        super(ServerMessageType.LOAD_GAME);
        this.username = username;
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }

    private final String username;
    private final int gameID;
}
