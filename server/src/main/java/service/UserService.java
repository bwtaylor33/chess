package service;

import dataaccess.AuthTokenDAO;
import dataaccess.DAOFactory;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import model.AuthData;
import model.request.LoginRequest;
import model.request.LogoutRequest;
import model.request.RegisterRequest;
import model.response.LoginResult;
import model.response.RegisterResult;

public class UserService {

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        // check and see if username is already in use
        UserDAO userDAO = DAOFactory.getUserDAO();
        try{
            UserData userData = userDAO.getUser(registerRequest.username);
            throw new ResponseException("Username is already in use: " + registerRequest.username);
        }catch (DataAccessException e) {
            // username is not already present
        }

        try{
            // create user in user table
            userDAO.createUser(new UserData(registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()));

            // automatically login to user by creating authToken
            AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();

            AuthData authData = new AuthData(registerRequest.username());
            authTokenDAO.createAuthToken(authData);

            // return the authToken
            return new RegisterResult(authData.getAuthToken());

        }catch (DataAccessException e) {
            throw new ResponseException("Error creating user: " + e.getMessage());
        }
    }

    public LoginResult login(LoginRequest loginRequest) {
        // check and see if username exists
        UserDAO userDAO = DAOFactory.getUserDAO();
        try{
            UserData userData = userDAO.getUser(loginRequest.username());
            // check for password match
            if (loginRequest.password() != userData.getPassword()) {
                throw new DataAccessException("Incorrect password");
            }

            // create authToken
            AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();

            AuthData authData = new AuthData(loginRequest.username());
            authTokenDAO.createAuthToken(authData);

            // return the authToken
            return new LoginResult(authData.getAuthToken());

        }catch (DataAccessException e) {
            throw new ResponseException("Error logging in user: " + e.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) {

    }

}