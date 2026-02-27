package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import model.request.RegisterRequest;
import model.response.RegisterResult;
import service.InvalidUsernameException;
import service.MissingBodyException;
import service.ResponseException;
import service.UserService;

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

        }catch (InvalidUsernameException i) {
            context.status(403).result(i.toJson());

        }catch (MissingBodyException m) {
            context.status(400).result(m.toJson());

        }catch (Exception e) {
            context.status(500).result("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private final UserService userService;
}
