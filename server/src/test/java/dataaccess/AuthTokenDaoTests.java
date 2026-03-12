package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthTokenDaoTests {

    @BeforeEach
    public void setUp() throws DataAccessException {

        authTokenDao = DaoFactory.getAuthTokenDao();
        authTokenDao.clearAllAuthTokens();
    }

    @Test
    public void testCreateAuthTokenSuccess() throws DataAccessException {

        AuthData testAuth = new AuthData("testUser");
        authTokenDao.createAuthToken(testAuth);
        AuthData verifiedAuth = authTokenDao.getAuthToken(testAuth.getAuthToken());

        Assertions.assertEquals(testAuth.getAuthToken(), verifiedAuth.getAuthToken());
        Assertions.assertEquals(testAuth.getUsername(), verifiedAuth.getUsername());
    }

    @Test
    public void testCreateAuthFailure() throws DataAccessException {

        AuthData testAuth = new AuthData("hardCodedAuthToken", "testUser");
        authTokenDao.createAuthToken(testAuth);

        // test for wrong authToken
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            authTokenDao.createAuthToken(testAuth);
        });
        Assertions.assertEquals("unable to update database: Duplicate entry 'hardCodedAuthToken' for key 'authtoken.PRIMARY'", exception.getMessage());
    }

    @Test
    public void testGetAuthSuccess() throws DataAccessException {

        // create 2 authTokens for same user
        AuthData testAuth = new AuthData("testUser");
        authTokenDao.createAuthToken(testAuth);

        AuthData testAuth2 = new AuthData("testUser");
        authTokenDao.createAuthToken(testAuth2);

        // test if 2 users were added to database
        AuthData verifiedAuth = authTokenDao.getAuthToken(testAuth.getAuthToken());
        Assertions.assertEquals(testAuth.getUsername(), verifiedAuth.getUsername());

        AuthData verifiedAuth2 = authTokenDao.getAuthToken(testAuth2.getAuthToken());
        Assertions.assertEquals(testAuth2.getUsername(), verifiedAuth2.getUsername());
    }

    @Test
    public void testGetAuthFailure() throws DataAccessException {

        // try to verify fake authToken
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            authTokenDao.getAuthToken("fakeAuthToken");
        });
        Assertions.assertEquals("Error: invalid authToken: fakeAuthToken", exception.getMessage());
    }

    @Test
    public void testClearAllAuthTokens() throws DataAccessException {

        AuthData testAuth = new AuthData("testUser");
        authTokenDao.createAuthToken(testAuth);

        // clear authTokens
        authTokenDao.clearAllAuthTokens();

        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            authTokenDao.getAuthToken(testAuth.getAuthToken());
        });
        Assertions.assertTrue(exception.getMessage().startsWith("Error: invalid authToken: "));
    }

    private AuthTokenDao authTokenDao;
}
