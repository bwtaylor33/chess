package server;

import io.javalin.*;
import service.*;

public class Server {

    private final Javalin javalin;

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
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
