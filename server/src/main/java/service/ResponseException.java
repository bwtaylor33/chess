package service;

import com.google.gson.Gson;
import org.eclipse.jetty.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;

/**
 * Exception used for capturing errors that occur when handling API requests.
 */
public class ResponseException extends Exception {

    public ResponseException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }

    public static ResponseException fromJson(String json) {
        var map = new Gson().fromJson(json, HashMap.class);
        String message = map.get("message").toString();
        return new ResponseException(message);
    }

    public static HttpStatus.Code fromHttpStatusCode(int httpStatusCode) {
        return switch (httpStatusCode) {
            case 500 -> HttpStatus.Code.ServerError;
            case 400 -> HttpStatus.Code.ClientError;
            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
        };
    }
}
