package server;

import io.javalin.*;
import server.websocket.WebSocketHandler;
import service.*;

/**
 * Our javalin server implementation. Registers all handlers.
 */
public class Server {

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // create services
        GameService gameService = new GameService();
        UserService userService = new UserService();

        // Register your endpoints and exception handlers here.
        ResetHandler resetHandler = new ResetHandler(userService, gameService);
        javalin.delete("/db", resetHandler::resetHandler);

        UserHandler userHandler = new UserHandler(userService);
        javalin.post("/user", userHandler::registerHandler);
        javalin.post("/session", userHandler::loginHandler);
        javalin.delete("/session", userHandler::logoutHandler);

        GameHandler gameHandler = new GameHandler(gameService);
        javalin.post("/game", gameHandler::createHandler);
        javalin.put("/game", gameHandler::joinHandler);
        javalin.get("/game", gameHandler::listGamesHandler);

        WebSocketHandler webSocketHandler = new WebSocketHandler();
        javalin.ws("/ws", ws -> {
            ws.onConnect(context -> {System.out.println("connect " + context.session.getUpgradeRequest().getRequestURI());});
            ws.onMessage(context -> {System.out.println("message: " + context.message());
            System.out.println(Thread.currentThread().getName());});
            ws.onClose(context -> {System.out.println("closed!!!!!");});
//            ws.onConnect(webSocketHandler::handleConnect);
//            ws.onMessage(webSocketHandler::handleMessage);
//            ws.onClose(webSocketHandler::handleClose);
        });
    }

    public int run(int desiredPort) {

        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private final Javalin javalin;
}
