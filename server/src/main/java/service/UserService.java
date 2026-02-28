package service;

import dataaccess.AuthTokenDAO;
import dataaccess.DAOFactory;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResult;
import model.response.RegisterResult;

/**
 * Service handles all user and authentication functions
 */
public class UserService extends BaseService {

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {

        if (registerRequest.username() == null || registerRequest.username().isBlank()) {
            throw new BadRequestException("Error: username cannot be blank");
        }

        if (registerRequest.password() == null || registerRequest.password().isBlank()) {
            throw new BadRequestException("Error: password cannot be blank");
        }

        // check and see if username is already in use
        UserDAO userDAO = DAOFactory.getUserDAO();

        try{
            UserData userData = userDAO.getUser(registerRequest.username());

            throw new ForbiddenRequestException("Error: username is already in use: " + registerRequest.username());
        }catch (DataAccessException e) {
            // username is not already present
        }

        try{
            // create user in user table
            userDAO.createUser(new UserData(registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()));

            // automatically login user by creating authToken
            AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();

            AuthData authData = new AuthData(registerRequest.username());
            authTokenDAO.createAuthToken(authData);

            // return the authToken
            return new RegisterResult(authData.getUsername(), authData.getAuthToken());

        }catch (DataAccessException e) {
            throw new ResponseException("Error creating user: " + e.getMessage());
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {

        if (loginRequest.username() == null) {
            throw new BadRequestException("Error: username cannot be null");
        }

        if (loginRequest.password() == null) {
            throw new BadRequestException("Error: password cannot be null");
        }

        // check and see if username exists
        UserDAO userDAO = DAOFactory.getUserDAO();
        try{
            UserData userData = userDAO.getUser(loginRequest.username());

            // check for password match
            if (!loginRequest.password().equals(userData.getPassword())) {
                throw new DataAccessException("Incorrect password");
            }

            // create authToken
            AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();

            AuthData authData = new AuthData(loginRequest.username());
            authTokenDAO.createAuthToken(authData);

            // return the authToken
            return new LoginResult(loginRequest.username(), authData.getAuthToken());

        }catch (DataAccessException e) {
            throw new ResponseException("Error logging in user: " + e.getMessage());
        }
    }

    public void logout(String authToken) {

        validateAuthToken(authToken);

        try{
            // delete user's authToken
            AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();

            authTokenDAO.deleteAuthToken(authToken);

        }catch (DataAccessException e) {
            throw new ResponseException("Error logging out user: " + e.getMessage());
        }
    }

    public void clear() {

        try {
            DAOFactory.getUserDAO().clearAllUsers();
            DAOFactory.getAuthTokenDAO().clearAllAuthTokens();

        }catch (DataAccessException d) {
            throw new ResponseException("Error clearing database: " + d.getMessage());
        }
    }
}