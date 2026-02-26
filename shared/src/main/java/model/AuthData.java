package model;

import java.util.UUID;

public class AuthData {
    private String authToken;
    private String username;

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public AuthData(String username) {
        this.authToken = getAuthToken();
        this.username = username;
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
