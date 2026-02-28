package service;

import dataaccess.DAOFactory;
import dataaccess.DataAccessException;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    @BeforeEach
    public void setUp() {

        userService = new UserService();
        userService.clear();

        authToken = userService.register(new RegisterRequest("testUsername", "testPassword", "test@junk.com")).authToken();
    }

    @Test
    public void testRegisterSuccess() throws DataAccessException {

        // register user
        userService.register(new RegisterRequest("testName", "testPass", "test@junk.com"));

        // verify data is in the database
        UserData userData = DAOFactory.getUserDAO().getUser("testName");
        Assertions.assertEquals("testName", userData.getUsername());
        Assertions.assertEquals("testPass", userData.getPassword());
        Assertions.assertEquals("test@junk.com", userData.getEmail());
    }

    @Test
    public void testRegisterFailure() {

        // test for username collision
        userService.register(new RegisterRequest("testName", "testPass", "test@junk.com"));

        Exception exception = Assertions.assertThrows(ForbiddenRequestException.class, () -> {
            userService.register(new RegisterRequest("testName", "testPass", "test@junk.com"));
        });
        Assertions.assertEquals("Error: username is already in use: testName", exception.getMessage());

        // test for blank password
        exception = Assertions.assertThrows(BadRequestException.class, () -> {
            userService.register(new RegisterRequest("testName1", " ", "test@junk.com"));
        });
        Assertions.assertEquals("Error: password cannot be blank", exception.getMessage());
    }

    @Test
    public void testLoginSuccess() {

        // login user again
        String authToken = userService.login(new LoginRequest("testUsername", "testPassword")).authToken();

        // confirm non-blank authToken
        Assertions.assertFalse(authToken.isBlank());
    }

    @Test
    public void testLoginFailure() {

        // test for invalid password
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.login(new LoginRequest("testUsername", "badPassword"));
        });
        Assertions.assertEquals("Error logging in user: Incorrect password", exception.getMessage());

        // test for unregistered user
        exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.login(new LoginRequest("badUsername", "testPassword"));
        });
        Assertions.assertEquals("Error logging in user: Error: invalid username: badUsername", exception.getMessage());
    }

    @Test
    public void testLogoutSuccess() {

        // log user out
        userService.logout(authToken);

        // confirm token is removed from database
        Assertions.assertThrows(DataAccessException.class, () -> {
            DAOFactory.getAuthTokenDAO().getAuthToken(authToken);
        });
    }

    @Test
    public void testLogoutFailure() {

        // logging out an invalid authToken
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            userService.logout("badToken");
        });
        Assertions.assertEquals("Error: invalid authToken: badToken", exception.getMessage());
    }

    @Test
    public void testClear() {

        // register 2 users
        userService.register(new RegisterRequest("testName", "testPass", "test@junk.com"));
        userService.register(new RegisterRequest("testName1", "testPass1", "test1@junk.com"));
        userService.clear();

        // test for removal of both users
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            DAOFactory.getUserDAO().getUser("testName");
        });
        Assertions.assertEquals("Error: invalid username: testName", exception.getMessage());

        exception = Assertions.assertThrows(DataAccessException.class, () -> {
            DAOFactory.getUserDAO().getUser("testName1");
        });
        Assertions.assertEquals("Error: invalid username: testName1", exception.getMessage());
    }

    private UserService userService;
    private String authToken;
}
