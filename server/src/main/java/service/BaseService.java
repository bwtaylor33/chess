package service;

import dataaccess.DaoFactory;
import dataaccess.DataAccessException;

/**
 * Base class for all service implementations. Contains code for authentication session validation
 */
public class BaseService {

    protected String validateAuthToken(String authToken) throws ResponseException {

        try{
            return DaoFactory.getAuthTokenDao().getAuthToken(authToken).getUsername();

        }catch (DataAccessException e) {
            throw new UnauthorizedRequestException("Error: " + e.getMessage());
        }
    }
}
