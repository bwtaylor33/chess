package server.websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DaoFactory;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        connectionManager.add(context.session);
        System.out.println("got connection from handleConnect");
    }

    @Override
    public void handleClose(@NotNull WsCloseContext context) throws Exception {
        connectionManager.remove(context.session);
        System.out.println("connection closed");
    }

    @Override
    public void handleMessage(WsMessageContext context) throws Exception {
        System.out.println("handling message");

        int gameID = -1;
        Session session = context.session;
        try {
            UserGameCommand command = Serializer.fromJson(context.message(), UserGameCommand.class);
            gameID = command.getGameID();
            String username = getUsername(command.getAuthToken());
            connectionManager.add(gameID, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }

        } catch (UnauthorizedException ex) {
            sendMessage(session, gameID, new ErrorMessage("Error: unauthorized"));

        } catch (Exception ex) {
            ex.printstackTrace();
            sendMessage(session, gameID, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void loadGame(int gameID) {

    }

    private void handleError(ErrorMessage errorMessage) {
        System.out.println(errorMessage);
    }

    private void handleNotification(NotificationMessage notificationMessage) {
        System.out.println(notificationMessage);
    }

    private String getUsername(String authToken) throws DataAccessException {
        return DaoFactory.getAuthTokenDao().getAuthToken(authToken).getUsername();
    }

    private final ConnectionManager connectionManager = new ConnectionManager();
}
