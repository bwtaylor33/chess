package dataaccess;

/**
 * Supports centralized access to all DAO objects. Constant defined here determines
 * whether a sql database is used or in memory storage.
 */
public class DaoFactory {

    public static AuthTokenDao getAuthTokenDAO() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return authTokenDAO;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    public static GameDao getGameDAO() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return gameDAO;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    public static UserDao getUserDAO() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return userDAO;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    private static final UserDao userDAO = new InMemoryUserDao();
    private static final GameDao gameDAO = new InMemoryGameDao();
    private static final AuthTokenDao authTokenDAO = new InMemoryAuthTokenDao();

    private static final boolean USE_IN_MEMORY_DATA_STORE = true;
}
