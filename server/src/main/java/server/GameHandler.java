package server;

import com.google.gson.Gson;
import io.javalin.http.Context;

import service.*;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;

/**
 * Handler for all game API calls
 */
public class GameHandler extends BaseHandler {

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void createHandler(Context context) {

        try {
            CreateGameRequest createGameRequest = getBodyObject(context, CreateGameRequest.class);
            CreateGameResult createGameResult = gameService.createGame(context.header("Authorization"), createGameRequest);

            // convert bodyObject back to Json and send to client
            context.json(new Gson().toJson(createGameResult));

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (UnauthorizedRequestException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    public void joinHandler(Context context) {

        try {
            JoinGameRequest joinGameRequest = getBodyObject(context, JoinGameRequest.class);
            gameService.joinGame(context.header("Authorization"), joinGameRequest);

            // return empty Json body
            context.status(200).result("{}");

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (UnauthorizedRequestException r) {
            context.status(401).result(r.toJson());

        }catch (ForbiddenRequestException f) {
            context.status(403).result(f.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    public void listGamesHandler(Context context) {

        try {
            ListGamesResult listGamesResult = gameService.listGames(context.header("Authorization"));

            // convert bodyObject back to Json and send to client
            context.json(new Gson().toJson(listGamesResult));

        }catch (UnauthorizedRequestException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private final GameService gameService;
}
