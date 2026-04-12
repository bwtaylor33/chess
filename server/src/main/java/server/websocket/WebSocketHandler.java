package server.websocket;

import dataaccess.DaoFactory;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.ForbiddenRequestException;
import websocket.commands.*;
import websocket.messages.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        System.out.println("got connection from handleConnect");
    }

    @Override
    public void handleClose(@NotNull WsCloseContext context) throws Exception {
        System.out.println("connection closed");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext context) throws Exception {
        System.out.println("handling message");

//        int gameID = -1;
//        Session session = context.session;
//        try {
//            UserGameCommand command = new Gson().fromJson(context.message(), UserGameCommand.class);
//            gameID = command.getGameID();
//            String username = getUsername(command.getAuthToken());
//            connectionManager.add(gameID, session);
//
//            switch (command.getCommandType()) {
//                case CONNECT:
//                    connect(session, username, (ConnectCommand) command);
//                    break;
//                case MAKE_MOVE:
//                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(context.message(), MakeMoveCommand.class);
//                    makeMove(session, username, makeMoveCommand);
//                    break;
//                case LEAVE:
//                    leaveGame(session, username, (LeaveGameCommand) command);
//                    break;
//                case RESIGN:
//                    resign(session, username, (ResignCommand) command);
//                    break;
//            }
//
//        } catch (ForbiddenRequestException ex) {
//            sendMessage(session, gameID, new ErrorMessage("Error: unauthorized"));
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            sendMessage(session, gameID, new ErrorMessage("Error: " + ex.getMessage()));
//        }
    }

    private void connect(Session session, String username, ConnectCommand command) {
        System.out.println("connect called for " + username);
        connectionManager.add(command.getGameID(), session);
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) {
        System.out.println("makeMove called for " + username);
    }

    private void leaveGame(Session session, String username, LeaveGameCommand command) {
        connectionManager.remove(command.getGameID(), session);
    }

    private void resign(Session session, String username, ResignGameCommand command) {
        System.out.println("resign called for " + username);
    }

    private void sendMessage(Session session, int gameID, ServerMessage message) {
        System.out.println("sendMessage called for " + gameID);
    }

    private String getUsername(String authToken) throws ForbiddenRequestException {

        try {
            return DaoFactory.getAuthTokenDao().getAuthToken(authToken).getUsername();

        } catch (DataAccessException d) {
            throw new ForbiddenRequestException(d.getMessage());
        }
    }

    private final ConnectionManager connectionManager = new ConnectionManager();
}