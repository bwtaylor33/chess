package dataaccess;

import model.AuthData;
import java.util.HashMap;

/**
 * In-memory authToken storage implementation
 */
public class InMemoryAuthTokenDAO implements AuthTokenDAO {

    public void createAuthToken(AuthData authData) throws DataAccessException {
        authTokens.put(authData, authData.getUsername());
    }

    public AuthData getAuthToken(String authToken) throws DataAccessException {

        for (AuthData authData: authTokens.keySet()) {
            if (authData.getAuthToken().equals(authToken)) {
                return authData;
            }
        }

        throw new DataAccessException("invalid authToken: " + authToken);
    }

    public void deleteAuthToken(String authToken) throws DataAccessException {

        AuthData deleteAuthData = null;
        for (AuthData authData : authTokens.keySet()) {
            if (authData.getAuthToken().equals(authToken)) {
                deleteAuthData = authData;
                break;
            }
        }

        // if authTokens not found, then just silently proceed
        if (deleteAuthData != null) {
            authTokens.remove(deleteAuthData);
        }
    }

    public void clearAllAuthTokens() throws DataAccessException {
        authTokens.clear();
    }

    final private HashMap<AuthData, String> authTokens = new HashMap<>();
}
