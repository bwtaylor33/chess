package websocket.messages;

public class NotificationMessage extends ServerMessage {

    public enum NotificationMessageType {
        CONNECT,
        MOVE,
        LEFT,
        RESIGN
    }

    public NotificationMessage(NotificationMessageType type, String username, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.type = type;
        this.username = username;
        this.message = message;
    }

    @Override
    public void display() {
        System.out.println(message);
    }

    private final NotificationMessageType type;
    private final String username;
    private final String message;
}
