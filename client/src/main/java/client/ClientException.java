package client;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Exception used for capturing errors that occur when handling API requests.
 */
public class ClientException extends Exception {

    public ClientException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }

    public static ClientException fromJson(String json) {
        var map = new Gson().fromJson(json, HashMap.class);
        String message = map.get("message").toString();
        return new ClientException(message);
    }
}
