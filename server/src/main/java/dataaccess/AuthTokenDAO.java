package dataaccess;

import model.AuthData;

public interface AuthTokenDAO {

    public void createAuthToken(AuthData authData) throws DataAccessException;
    public AuthData getAuthToken(String username) throws DataAccessException;
    public void updateAuthToken(AuthData authData) throws DataAccessException;
    public void deleteAuthToken(String username) throws  DataAccessException;
    public void clearAllAuthTokens() throws DataAccessException;
}
