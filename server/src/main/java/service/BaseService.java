package service;

import dataaccess.AuthTokenDao;
import dataaccess.DaoFactory;
import dataaccess.DataAccessException;

/**
 * Base class for all service implementations. Contains code for authentication session validation
 */
public class BaseService {

    protected void validateAuthToken(String authToken) throws ResponseException {

        try{
            // create authToken
            AuthTokenDao authTokenDao = DaoFactory.getAuthTokenDao();

            authTokenDao.getAuthToken(authToken);

        }catch (DataAccessException e) {
            throw new ResponseException("Error: " + e.getMessage());
        }
    }
}
