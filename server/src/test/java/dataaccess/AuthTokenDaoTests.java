package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthTokenDaoTests {

    @BeforeEach
    public void setUp() throws DataAccessException {

        userDao = DaoFactory.getUserDao();
        userDao.clearAllUsers();
    }

    @Test
    public void testCreateUserSuccess() throws DataAccessException {

        UserData testUser = new UserData("testNewUser", "testNewPassword", "testNewEmail");
        userDao.createUser(testUser);
        UserData verifiedUser = userDao.getUser("testNewUser");

        Assertions.assertEquals(testUser.getEmail(), verifiedUser.getEmail());
        Assertions.assertEquals(testUser.getUsername(), verifiedUser.getUsername());
    }

    @Test
    public void testCreateUserFailure() throws DataAccessException {

        UserData testUser = new UserData("testNewUser", "testNewPassword", "testNewEmail");
        userDao.createUser(testUser);

        // test for duplicate username
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            userDao.createUser(testUser);
        });
        Assertions.assertEquals("unable to update database: Duplicate entry 'testNewUser' for key 'user.PRIMARY'", exception.getMessage());
    }

    @Test
    public void testGetUserSuccess() throws DataAccessException {

        // create 2 users
        UserData testUser = new UserData("testNewUser", "testNewPassword", "testNewEmail");
        userDao.createUser(testUser);

        UserData testUser2 = new UserData("testNewUser2", "testNewPassword2", "testNewEmail2");
        userDao.createUser(testUser2);

        // test if 2 users were added to database
        UserData verifiedUser = userDao.getUser("testNewUser");
        Assertions.assertEquals(testUser.getEmail(), verifiedUser.getEmail());
        Assertions.assertEquals(testUser.getUsername(), verifiedUser.getUsername());

        UserData verifiedUser2 = userDao.getUser("testNewUser2");
        Assertions.assertEquals(testUser2.getEmail(), verifiedUser2.getEmail());
        Assertions.assertEquals(testUser2.getUsername(), verifiedUser2.getUsername());
    }

    @Test
    public void testGetUserFailure() throws DataAccessException {

        // call getUser before createUser
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            userDao.getUser("testNewUser");
        });
        Assertions.assertEquals("Error: invalid username: testNewUser", exception.getMessage());
    }

    @Test
    public void testClearAllUsers() throws DataAccessException {

        UserData testUser = new UserData("testNewUser", "testNewPassword", "testNewEmail");
        userDao.createUser(testUser);

        // clear users
        userDao.clearAllUsers();

        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            userDao.getUser("testNewUser");
        });
        Assertions.assertEquals("Error: invalid username: testNewUser", exception.getMessage());
    }

    private UserDao userDao;
}
