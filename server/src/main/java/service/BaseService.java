package service;

import dataaccess.AuthTokenDAO;
import dataaccess.DAOFactory;
import dataaccess.DataAccessException;
import model.AuthData;
import model.request.LogoutRequest;

public class BaseService {

    protected void validateAuthToken(String authToken) throws ResponseException {
        System.out.println("validating authToken: " + authToken);
        try{

            // create authToken
            AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();

            authTokenDAO.getAuthToken(authToken);

        }catch (DataAccessException e) {
            throw new ResponseException("Error: invalid authToken " + e.getMessage());
        }
    }
}
