package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        ArrayList<Session> set = connections.computeIfAbsent(gameID, k -> new ArrayList<>());
        set.add(session);
    }

    public void remove(int gameID, Session session) {
        ArrayList<Session> set = connections.computeIfAbsent(gameID, k -> new ArrayList<>());
        set.remove(session);
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage serverMessage) throws IOException {

        String msg = serverMessage.toString();
        ArrayList<Session> set = connections.computeIfAbsent(gameID, k -> new ArrayList<>());

        for (Session s : set) {
            if (s.isOpen()) {
                if (!s.equals(excludeSession)) {
                    s.getRemote().sendString(msg);
                }
            }
        }
    }
}