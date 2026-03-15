package dataaccess;

/**
 * Supports centralized access to all Dao objects. Constant defined here determines
 * whether a sql database is used or in memory storage.
 */
public class DaoFactory {

    public static AuthTokenDao getAuthTokenDao() throws DataAccessException {

        if (authTokenDao == null) {
            authTokenDao = USE_IN_MEMORY_DATA_STORE ? new InMemoryAuthTokenDao() : new MySqlAuthTokenDao();
        }

        return authTokenDao;
    }

    public static GameDao getGameDao() throws DataAccessException {

        if (gameDao == null) {
            gameDao = USE_IN_MEMORY_DATA_STORE ? new InMemoryGameDao() : new MySqlGameDao();
        }

        return gameDao;
    }

    public static UserDao getUserDao() throws DataAccessException {

        if (userDao == null) {
            userDao = USE_IN_MEMORY_DATA_STORE ? new InMemoryUserDao() : new MySqlUserDao();
        }

        return userDao;
    }

    private static UserDao userDao = null;
    private static GameDao gameDao = null;
    private static AuthTokenDao authTokenDao = null;

    private static final boolean USE_IN_MEMORY_DATA_STORE = false;
}
