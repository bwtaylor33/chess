package websocket.messages;

public class ErrorMessage extends ServerMessage {

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    @Override
    public void display() {
        System.out.println(errorMessage);
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    private final String errorMessage;
}