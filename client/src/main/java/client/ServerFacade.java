package client;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import jakarta.websocket.*;

import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;
import model.response.LoginResult;
import model.response.RegisterResult;
import websocket.commands.*;
import websocket.messages.*;

public class ServerFacade extends Endpoint {

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ClientException {
        var request = buildRequest(null, "POST", "/user", registerRequest);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) throws ClientException {
        var request = buildRequest(null, "POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void logout(String authToken) throws ClientException {
        var request = buildRequest(authToken, "DELETE", "/session", null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws ClientException {
        var request = buildRequest(authToken, "POST", "/game", createGameRequest);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class);
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ClientException {
        var request = buildRequest(authToken, "PUT", "/game", joinGameRequest);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public ListGamesResult listGames(String authToken) throws ClientException {
        var request = buildRequest(authToken, "GET", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class);
    }

    public void setServerMessageConsumer(ServerMessageConsumer serverMessageConsumer) {
        this.serverMessageConsumer = serverMessageConsumer;
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    public void sendCommand(UserGameCommand command) throws ClientException {

        if(session == null) {

            // Create websocket connection
            try{
                session = ContainerProvider.getWebSocketContainer().connectToServer(this, new URI(serverUrl.replace("http", "ws")+ "/ws"));

            }catch(Exception x) {
                throw new ClientException("Error: creating websocket URI: " + x.getMessage());
            }

            // Wire in our ServerMessageConsumer

            if(serverMessageConsumer == null) {
                throw new ClientException("No consumer yet assigned for server messages");
            }

            session.addMessageHandler(new MessageHandler.Whole<String>(){

                @Override
                public void onMessage(String message){

                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);

                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION:
                            serverMessageConsumer.notify(gson.fromJson(message, NotificationMessage.class));
                            break;
                        case LOAD_GAME:
                            serverMessageConsumer.loadGame(gson.fromJson(message, LoadGameMessage.class));
                            break;
                        case ERROR:
                            serverMessageConsumer.error(gson.fromJson(message, ErrorMessage.class));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized server message: " + serverMessage);
                    }
                }
            });
        }

        if (session.isOpen()) {
            session.getAsyncRemote().sendText(command.toJson());
        }
    }

    private HttpRequest buildRequest(String authToken, String method, String path, Object body) {

        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));

        if (authToken != null) {
            request.setHeader("Authorization", authToken);
        }

        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }

        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {

        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));

        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ClientException {

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception ex) {
            throw new ClientException("Error: Send request failure: " + ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ClientException {

        var status = response.statusCode();

        if (!isSuccessful(status)) {

            var body = response.body();
            if (body != null) {
                throw ClientException.fromJson(body);
            }

            throw new ClientException("Error: other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private Session session = null;
    private ServerMessageConsumer serverMessageConsumer = null;
}