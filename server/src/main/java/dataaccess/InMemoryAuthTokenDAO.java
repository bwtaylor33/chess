package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class InMemoryAuthTokenDAO implements AuthTokenDAO {

    public void createAuthToken(AuthData authData) throws DataAccessException {
        updateAuthToken(authData);
    }

    public AuthData getAuthToken(String username) throws DataAccessException {
        if (!authTokens.containsKey(username)) {
            throw new DataAccessException("No authToken found for user: " + username);
        }

        return authTokens.get(username);
    }

    public void updateAuthToken(AuthData authData) throws DataAccessException {
        authTokens.put(authData.getUsername(), authData);
    }

    public void deleteAuthToken(String username) throws  DataAccessException {
        if (!authTokens.containsKey(username)) {
            throw new DataAccessException("authToken not found for user: " + username);
        }

        authTokens.remove(username);
    }

    public void clearAllAuthTokens() throws DataAccessException {
        authTokens = new HashMap<>();
    }

    private HashMap<String, AuthData> authTokens = new HashMap<>();
}
