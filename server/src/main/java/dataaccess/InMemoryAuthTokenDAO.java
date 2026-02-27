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

        //if authTokens not found, then just silently proceed
        if (authTokens.containsValue(authData)) {
            authTokens.remove(authData.getUsername());
        }
    }

    public void clearAllAuthTokens() throws DataAccessException {
        authTokens = new HashMap<>();
    }

    private HashMap<String, AuthData> authTokens = new HashMap<>();
}
