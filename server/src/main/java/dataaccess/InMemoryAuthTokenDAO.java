package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class InMemoryAuthTokenDAO implements AuthTokenDAO {

    public void createAuthToken(AuthData authData) throws DataAccessException {
        updateAuthToken(authData);
    }

    public AuthData getAuthToken(String authToken) throws DataAccessException {

        AuthData authData = new AuthData(authToken, "");
        for (String username: authTokens.keySet()) {
            if (authTokens.get(username).getAuthToken().equals(authToken)) {
                authData.setUsername(username);
                System.out.println("Matched username to token: " + username);
                break;
            }
        }

        if (authData.getUsername().isEmpty()) {
            throw new DataAccessException("Invalid authToken: " + authToken);
        }

        return authData;
    }

    public void updateAuthToken(AuthData authData) throws DataAccessException {
        authTokens.put(authData.getUsername(), authData);
    }

    public void deleteAuthToken(String authToken) throws  DataAccessException {
        AuthData authData = getAuthToken(authToken);
        System.out.println("Deleting: " + authToken);
        System.out.println("authTokens size: " + authTokens.size());

        //if authTokens not found, then just silently proceed
        if (authTokens.containsKey(authData.getUsername())) {
            System.out.println("Database contains authData");
            authTokens.remove(authData.getUsername());
        }
        System.out.println("post authTokens size: " + authTokens.size());
    }

    public void clearAllAuthTokens() throws DataAccessException {
        authTokens = new HashMap<>();
    }

    private HashMap<String, AuthData> authTokens = new HashMap<>();
}
