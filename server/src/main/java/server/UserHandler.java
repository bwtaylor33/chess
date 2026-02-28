package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResult;
import model.response.RegisterResult;
import service.*;

/**
 * Handler for all user API calls
 */
public class UserHandler extends BaseHandler {

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public void registerHandler(Context context) {

        try {
            RegisterRequest registerRequest = getBodyObject(context, RegisterRequest.class);
            RegisterResult registerResult = userService.register(registerRequest);

            // Convert bodyObject back to json and send to client
            context.json(new Gson().toJson(registerResult));

        }catch (ForbiddenRequestException f) {
            context.status(403).result(f.toJson());

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    public void loginHandler(Context context) {

        try {
            LoginRequest loginRequest = getBodyObject(context, LoginRequest.class);
            LoginResult loginResult = userService.login(loginRequest);

            // Convert bodyObject back to json and send to client
            context.json(new Gson().toJson(loginResult));

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (ResponseException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    public void logoutHandler(Context context) {

        try {
            userService.logout(context.header("Authorization"));

        }catch (BadRequestException m) {
            context.status(400).result(m.toJson());

        }catch (ResponseException r) {
            context.status(401).result(r.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private final UserService userService;
}
