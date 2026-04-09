package websocket.messages;

public class NotificationMessage extends ServerMessage {

    public enum NotificationMessageType {
        CONNECT,
        MOVE,
        LEFT,
        RESIGN
    }

    public NotificationMessage(NotificationMessageType type, String username, String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.type = type;
        this.username = username;
        this.notification = notification;
    }

    private final NotificationMessageType type;
    private final String username;
    private final String notification;
}
