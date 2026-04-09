package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Session> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        connections.put(gameID, session);
    }

    public void remove(int gameID) {
        connections.remove(gameID);
    }

    public void broadcast(Session excludeSession, ServerMessage serverMessage) throws IOException {
        String msg = serverMessage.toString();
        for (Session c : connections.values()) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}