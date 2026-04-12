package server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;

import websocket.messages.ServerMessage;

import javax.swing.plaf.synth.SynthDesktopIconUI;

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
        String message = serverMessage.toString();

        System.out.println("Sending direct message: " + serverMessage.getServerMessageType());
        if (serverMessage instanceof websocket.messages.ErrorMessage){
            websocket.messages.ErrorMessage errMsg = (websocket.messages.ErrorMessage)serverMessage;
            System.out.println("errorMessage: " + errMsg.getErrorMessage());
        }
        synchronized(session){
            session.getRemote().sendString(message);
        }
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage serverMessage) throws IOException {

        String message = serverMessage.toString();
        System.out.println("Broadcasting message: " + serverMessage.getServerMessageType());
        ArrayList<Session> set = connections.computeIfAbsent(gameID, k -> new ArrayList<>());

        for (Session s : set) {
            if (s.isOpen()) {
                if (!s.equals(excludeSession)) {
                    synchronized(s){
                        s.getRemote().sendString(message);
                    }
                }
            }
        }
    }

    private final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();
}