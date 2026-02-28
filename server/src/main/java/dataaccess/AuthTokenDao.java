package dataaccess;

import model.AuthData;

/**
 * Authentication token DAO interface
 */
public interface AuthTokenDao {

    public void createAuthToken(AuthData authData) throws DataAccessException;
    public AuthData getAuthToken(String authToken) throws DataAccessException;
    public void deleteAuthToken(String authToken) throws  DataAccessException;
    public void clearAllAuthTokens() throws DataAccessException;
}