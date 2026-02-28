package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.BadRequestException;

/**
 * Base class for all handlers. Contains code for conversion of JSON to request model objects
 */
public class BaseHandler {

    protected BaseHandler() {
    }

    protected static <T> T getBodyObject(Context context, Class<T> clazz) throws BadRequestException {

        var bodyObject = new Gson().fromJson(context.body(), clazz);

        if (bodyObject == null) {
            throw new BadRequestException("Error: body cannot be empty");
        }

        return bodyObject;
    }
}
