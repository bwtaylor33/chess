package model;

import java.util.UUID;

public class AuthData {

    public AuthData(String username) {
        this.authToken = generateToken();
        this.username = username;
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

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private String authToken;
    private String username;
}
