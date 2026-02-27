package service;

import com.google.gson.Gson;

import java.util.Map;

public class ResponseException extends RuntimeException {

    public ResponseException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }
}
