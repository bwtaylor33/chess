package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.MissingBodyException;

public class BaseHandler {

    public BaseHandler() {
    }

    protected void validateAuthToken() {

    }

    protected static <T> T getBodyObject(Context context, Class<T> clazz) throws MissingBodyException {
        var bodyObject = new Gson().fromJson(context.body(), clazz);

        if (bodyObject == null) {
            throw new MissingBodyException();
        }

        return bodyObject;
    }
}
