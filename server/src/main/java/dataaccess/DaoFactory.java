package dataaccess;

/**
 * Supports centralized access to all Dao objects. Constant defined here determines
 * whether a sql database is used or in memory storage.
 */
public class DaoFactory {

    public static AuthTokenDao getAuthTokenDao() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return AUTH_TOKEN_DAO;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    public static GameDao getGameDao() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return GAME_DAO;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    public static UserDao getUserDao() {

        if (USE_IN_MEMORY_DATA_STORE) {
            return USER_DAO;
        }

        throw new RuntimeException("Sql access not implemented");
    }

    private static final UserDao USER_DAO = new InMemoryUserDao();
    private static final GameDao GAME_DAO = new InMemoryGameDao();
    private static final AuthTokenDao AUTH_TOKEN_DAO = new InMemoryAuthTokenDao();

    private static final boolean USE_IN_MEMORY_DATA_STORE = true;
}
