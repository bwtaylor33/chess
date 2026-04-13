package server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;

import websocket.messages.ServerMessage;

public class ConnectionManager {

    public void add(int gameID, Session session) {
        ArrayList<Session> set = connections.computeIfAbsent(gameID, k -> new ArrayList<>());
        set.add(session);
    }

    public void remove(int gameID, Session session) {
        ArrayList<Session> set = connections.computeIfAbsent(gameID, k -> new ArrayList<>());
        set.remove(session);
    }

    public void send(Session session, ServerMessage serverMessage) throws IOException {

        // debug logging
        if (serverMessage instanceof websocket.messages.ErrorMessage){
            websocket.messages.ErrorMessage errMsg = (websocket.messages.ErrorMessage)serverMessage;
            System.out.println("Sending errorMessage: " + errMsg.getErrorMessage());
        }
        if (serverMessage instanceof websocket.messages.NotificationMessage){
            websocket.messages.NotificationMessage notifMsg = (websocket.messages.NotificationMessage)serverMessage;
            System.out.print("Sending notification: ");
            notifMsg.display();
        }
        if (serverMessage instanceof websocket.messages.LoadGameMessage){
            websocket.messages.LoadGameMessage loadMsg = (websocket.messages.LoadGameMessage)serverMessage;
            System.out.printf("Sending load: username=%s\n", loadMsg.getUsername());
        }

        synchronized(session){
            session.getRemote().sendString(serverMessage.toString());
        }
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage serverMessage) throws IOException {

        // debug logging
        if (serverMessage instanceof websocket.messages.ErrorMessage){
            websocket.messages.ErrorMessage errMsg = (websocket.messages.ErrorMessage)serverMessage;
            System.out.println("Broadcasting errorMessage: " + errMsg.getErrorMessage());
        }
        if (serverMessage instanceof websocket.messages.NotificationMessage){
            websocket.messages.NotificationMessage notifMsg = (websocket.messages.NotificationMessage)serverMessage;
            System.out.print("Broadcasting notification: ");
            notifMsg.display();
        }
        if (serverMessage instanceof websocket.messages.LoadGameMessage){
            websocket.messages.LoadGameMessage loadMsg = (websocket.messages.LoadGameMessage)serverMessage;
            System.out.printf("Broadcasting load: username=%s\n", loadMsg.getUsername());
        }

        ArrayList<Session> set = connections.computeIfAbsent(gameID, k -> new ArrayList<>());

        for (Session s : set) {
            if (s.isOpen()) {
                if (!s.equals(excludeSession)) {
                    synchronized(s){
                        s.getRemote().sendString(serverMessage.toString());
                    }
                }
            }
        }
    }

    private final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();
}