package service;

import dataaccess.AuthTokenDao;
import dataaccess.DaoFactory;
import dataaccess.DataAccessException;
import dataaccess.UserDao;
import model.UserData;
import model.AuthData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.LoginResult;
import model.response.RegisterResult;
import org.mindrot.jbcrypt.BCrypt;

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
        UserDao userDao = null;

        try{
            userDao = DaoFactory.getUserDao();

            UserData userData = userDao.getUser(registerRequest.username());

            throw new ForbiddenRequestException("Error: username is already in use: " + registerRequest.username());

        }catch (DataAccessException e) {
            // username is not already present
        }

        try{
            userDao = DaoFactory.getUserDao();

            // create user in user table
            userDao.createUser(new UserData(registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()));

            // automatically login user by creating authToken
            AuthTokenDao authTokenDao = DaoFactory.getAuthTokenDao();

            AuthData authData = new AuthData(registerRequest.username());
            authTokenDao.createAuthToken(authData);

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

        try{
            // check and see if username exists
            UserDao userDao = DaoFactory.getUserDao();

            UserData userData = null;
            try {
                userData = userDao.getUser(loginRequest.username());

            } catch (DataAccessException d) {
                throw new UnauthorizedRequestException("Error: Invalid username: " + loginRequest.username());
            }

            // check for password match
            if (!BCrypt.checkpw(loginRequest.password(), userData.getPassword())) {
                throw new UnauthorizedRequestException("Error: Incorrect password");
            }

            // create authToken
            AuthTokenDao authTokenDao = DaoFactory.getAuthTokenDao();

            AuthData authData = new AuthData(loginRequest.username());
            authTokenDao.createAuthToken(authData);

            // return the authToken
            return new LoginResult(loginRequest.username(), authData.getAuthToken());

        }catch (DataAccessException e) {
            throw new UnauthorizedRequestException("Error logging in user: " + e.getMessage());
        }
    }

    public void logout(String authToken) throws ResponseException {

        validateAuthToken(authToken);

        try{
            // delete user's authToken
            AuthTokenDao authTokenDao = DaoFactory.getAuthTokenDao();

            authTokenDao.deleteAuthToken(authToken);

        }catch (DataAccessException e) {
            throw new ResponseException("Error logging out user: " + e.getMessage());
        }
    }

    public void clear() throws ResponseException {

        try {
            DaoFactory.getUserDao().clearAllUsers();
            DaoFactory.getAuthTokenDao().clearAllAuthTokens();

        }catch (DataAccessException d) {
            throw new ResponseException("Error clearing database: " + d.getMessage());
        }
    }
}