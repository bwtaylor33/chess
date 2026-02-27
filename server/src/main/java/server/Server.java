package server;

import io.javalin.*;
import service.InvalidUsernameException;
import service.ResponseException;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        UserHandler userHandler = new UserHandler(new UserService());
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
