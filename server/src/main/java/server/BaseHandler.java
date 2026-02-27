package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.BadRequestException;

public class BaseHandler {

    public BaseHandler() {
    }

    protected static <T> T getBodyObject(Context context, Class<T> clazz) throws BadRequestException {
        var bodyObject = new Gson().fromJson(context.body(), clazz);

        if (bodyObject == null) {
            throw new BadRequestException("Error: body cannot be empty");
        }

        return bodyObject;
    }
}
