package server.websocket;

import dataaccess.DaoFactory;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import com.google.gson.Gson;
import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.ForbiddenRequestException;
import service.ResponseException;
import websocket.commands.*;
import websocket.messages.*;
import service.GameService;
import service.UnauthorizedRequestException;
import model.GameData;
import model.request.JoinGameRequest;
import websocket.commands.LeaveGameCommand;
import websocket.commands.ResignGameCommand;
import websocket.commands.MakeMoveCommand;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler, WsErrorHandler {

    @Override
    public void handleConnect(@NotNull WsConnectContext context) throws Exception {
        System.out.println("connection opened");
        // TODO: What do we need to do for the keepalive behavior, so things don't time out at 30s?
    }

    @Override
    public void handleClose(@NotNull WsCloseContext context) throws Exception {
        // TODO: Do we need to do any kind of auto-exit on observing games or playing games?
        System.out.println("connection closed");
    }

    @Override
    public void handleError(@NotNull WsErrorContext context) { //TODO: Error handler for debugging only
        System.err.println("WebSocket error on session: " + context.sessionId());
        context.error().printStackTrace();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext context) throws Exception {

        int gameID = -1;
        Session session = context.session;

        try {
            //System.out.println("RAW COMMAND: " + context.message()); //TODO: Remove raw each of socket message inbound to client
            Gson gson = new Gson();

            UserGameCommand command = gson.fromJson(context.message(), UserGameCommand.class);
            gameID = command.getGameID();
            String username = getUsername(command.getAuthToken());

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, gson.fromJson(context.message(), ConnectCommand.class));
                case MAKE_MOVE -> makeMove(session, username, gson.fromJson(context.message(), MakeMoveCommand.class));
                case LEAVE -> leaveGame(session, username, gson.fromJson(context.message(), LeaveGameCommand.class));
                case RESIGN -> resign(session, username, gson.fromJson(context.message(), ResignGameCommand.class));
            }

        } catch (UnauthorizedRequestException e) {
            connectionManager.send(session, new ErrorMessage("Error: Unauthorized: " + e.getMessage()));

        } catch (Exception e) {
            //e.printStackTrace(); //TODO:Clean up after debugging
            connectionManager.send(session, new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private void connect(Session session, String username, ConnectCommand command) throws ResponseException, IOException {

        // Sign up for broadcast messages
        connectionManager.add(command.getGameID(), session);

        // Retrieve game details
        GameData gameData = gameService.getGame(command.getAuthToken(), command.getGameID());

        // test if Game null


        // Respond with the load game message
        System.out.println("loaded game: " + gameData.getGame().toString());
        connectionManager.send(session, new LoadGameMessage(username, gameData.getGame()));

        // Broadcast an update to watchers
        connectionManager.broadcast(command.getGameID(), session,
                new NotificationMessage(NotificationMessage.NotificationMessageType.CONNECT, username,
                        String.format("%s connected to game %d.", username, command.getGameID())));
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws ResponseException, IOException {

        String moveMessage = null;

        try {
            moveMessage = gameService.makeMove(command.getAuthToken(), command);

        } catch (ResponseException x){
            connectionManager.send(session, new ErrorMessage(x.getMessage()));
            return;
        }

        // Broadcast an update to watchers
        connectionManager.broadcast(command.getGameID(), session,
                new NotificationMessage(NotificationMessage.NotificationMessageType.MOVE, username, moveMessage));

        // Broadcast a board update
        connectionManager.broadcast(command.getGameID(), session,
                new LoadGameMessage(username, gameService.getGame(command.getAuthToken(), command.getGameID()).getGame()));
    }

    private void leaveGame(Session session, String username, LeaveGameCommand command) throws ResponseException, IOException {

        // Leave the game
        gameService.leaveGame(command.getAuthToken(), command);

        // Remove this one from active watchers
        connectionManager.remove(command.getGameID(), session);

        // Broadcast an update to watchers
        connectionManager.broadcast(command.getGameID(), session,
                new NotificationMessage(NotificationMessage.NotificationMessageType.LEFT, username,
                        String.format("%s left game %d", username, command.getGameID())));
    }

    private void resign(Session session, String username, ResignGameCommand command) throws ResponseException, IOException {

        // Resign game
        gameService.resignGame(command.getAuthToken(), command);

        // Remove this one from active watchers
        connectionManager.remove(command.getGameID(), session);

        // Broadcast an update to watchers
        connectionManager.broadcast(command.getGameID(), session,
                new NotificationMessage(NotificationMessage.NotificationMessageType.RESIGN, username,
                        String.format("%s resigned game %d.", username, command.getGameID())));
    }

    private String getUsername(String authToken) throws ForbiddenRequestException {

        try {
            return DaoFactory.getAuthTokenDao().getAuthToken(authToken).getUsername();

        } catch (DataAccessException d) {
            throw new ForbiddenRequestException(d.getMessage());
        }
    }

    private final ConnectionManager connectionManager = new ConnectionManager();
    private final GameService gameService = new GameService();
}