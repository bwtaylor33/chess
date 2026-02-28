package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;
import service.*;

import java.util.ArrayList;

public class GameHandler extends BaseHandler {

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void createHandler(Context context) {
        try {
            CreateGameRequest createGameRequest = getBodyObject(context, CreateGameRequest.class);
            CreateGameResult createGameResult = gameService.createGame(context.header("Authorization"), createGameRequest.gameName());

            // Convert bodyObject back to Json and send to client
            context.json(new Gson().toJson(createGameResult));

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (ResponseException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    public void joinHandler(Context context) {
        try {
            JoinGameRequest joinGameRequest = getBodyObject(context, JoinGameRequest.class);
            gameService.joinGame(context.header("Authorization"), joinGameRequest.playerColor(), joinGameRequest.gameID());

            // return empty Json body
            context.status(200).result("{}");

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (ForbiddenRequestException f) {
            context.status(403).result(f.toJson());

        }catch (ResponseException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    public void listGamesHandler(Context context) {
        try {
            ListGamesResult listGamesResult = gameService.listGames(context.header("Authorization"));

            // Convert bodyObject back to Json and send to client
            context.json(new Gson().toJson(listGamesResult));

        }catch (ResponseException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private final GameService gameService;
}
