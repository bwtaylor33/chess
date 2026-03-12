package dataaccess;

import model.UserData;
import model.request.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BadRequestException;

public class UserDaoTests {

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

    private UserDao userDao;
}
