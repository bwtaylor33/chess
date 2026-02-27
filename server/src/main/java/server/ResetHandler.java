package server;

import io.javalin.http.Context;
import service.*;

public class ResetHandler extends BaseHandler {

    public ResetHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void resetHandler(Context context) {
        try {

            userService.clear();
            gameService.clear();

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (ResponseException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private final UserService userService;
    private final GameService gameService;
}
