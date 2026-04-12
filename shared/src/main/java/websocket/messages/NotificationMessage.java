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
        switch(type){
            case CONNECT:
                System.out.printf("Connect: %s", message);
                break;
            case MOVE:
                System.out.printf("Move: %s", message);
                break;
            case LEFT:
                System.out.printf("Left: %s", message);
                break;
            case RESIGN:
                System.out.printf("Resign: %s", message);
                break;
            default:
                break;
        }
    }

    private final NotificationMessageType type;
    private final String username;
    private final String message;
}
