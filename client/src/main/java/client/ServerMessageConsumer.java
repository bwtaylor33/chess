package client;

import websocket.messages.NotificationMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ErrorMessage;


public interface ServerMessageConsumer {

    public void notify(NotificationMessage notificationMessage);
    public void loadGame(LoadGameMessage loadGameMessage);
    public void error(ErrorMessage errorMessage);
}