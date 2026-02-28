package service;

import dataaccess.AuthTokenDAO;
import dataaccess.DAOFactory;
import dataaccess.DataAccessException;

/**
 * Base class for all service implementations. Contains code for authentication session validation
 */
public class BaseService {

    protected void validateAuthToken(String authToken) throws ResponseException {

        try{
            // create authToken
            AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();

            authTokenDAO.getAuthToken(authToken);

        }catch (DataAccessException e) {
            throw new ResponseException("Error: " + e.getMessage());
        }
    }
}
