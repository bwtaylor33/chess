package dataaccess;

import model.AuthData;
import java.util.HashMap;

public class InMemoryAuthTokenDAO implements AuthTokenDAO {

    public void createAuthToken(AuthData authData) throws DataAccessException {
        authTokens.put(authData, authData.getUsername());
    }

    public AuthData getAuthToken(String authToken) throws DataAccessException {

        for (AuthData authData: authTokens.keySet()) {
            if (authData.getAuthToken().equals(authToken)) {
                // System.out.println("Matched username to token: " + authData.getUsername());
                return authData;
            }
        }

        throw new DataAccessException("Invalid authToken: " + authToken);
    }

    public void deleteAuthToken(String authToken) throws  DataAccessException {

        AuthData deleteAuthData = null;
        for (AuthData authData : authTokens.keySet()) {
            if (authData.getAuthToken().equals(authToken)) {
                deleteAuthData = authData;
                break;
            }
        }

        // if authTokens not found, then just silently proceed
        if (deleteAuthData != null) {
            // System.out.println("Database contains authData");
            authTokens.remove(deleteAuthData);
        }
    }

    public void clearAllAuthTokens() throws DataAccessException {
        authTokens = new HashMap<>();
    }

    private HashMap<AuthData, String> authTokens = new HashMap<>();
}
