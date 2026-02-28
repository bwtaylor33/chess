package dataaccess;

/**
 * Supports centralized access to all Dao objects. Constant defined here determines
 * whether a sql database is used or in memory storage.
 */
public class DaoFactory {

    public static AuthTokenDao getAuthTokenDao() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return authTokenDao;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    public static GameDao getGameDao() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return gameDao;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    public static UserDao getUserDao() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return userDao;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    private static final UserDao userDao = new InMemoryUserDao();
    private static final GameDao gameDao = new InMemoryGameDao();
    private static final AuthTokenDao authTokenDao = new InMemoryAuthTokenDao();

    private static final boolean USE_IN_MEMORY_DATA_STORE = true;
}
