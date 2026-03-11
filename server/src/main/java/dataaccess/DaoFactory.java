package dataaccess;

/**
 * Supports centralized access to all Dao objects. Constant defined here determines
 * whether a sql database is used or in memory storage.
 */
public class DaoFactory {

    public static AuthTokenDao getAuthTokenDao() {

        if (AUTH_TOKEN_DAO == null) {
            AUTH_TOKEN_DAO = USE_IN_MEMORY_DATA_STORE ? new InMemoryAuthTokenDao() : new MySqlAuthTokenDao();
        }

        return AUTH_TOKEN_DAO;
    }

    public static GameDao getGameDao() {

        if (GAME_DAO == null) {
            GAME_DAO = USE_IN_MEMORY_DATA_STORE ? new InMemoryGameDao() : new MySqlGameDao();
        }

        return GAME_DAO;
    }

    public static UserDao getUserDao() {

        if (USER_DAO == null) {
            USER_DAO = USE_IN_MEMORY_DATA_STORE ? new InMemoryUserDao() : new MySqlUserDao();
        }

        return USER_DAO;
    }

    private static UserDao USER_DAO = null;
    private static GameDao GAME_DAO = null;
    private static AuthTokenDao AUTH_TOKEN_DAO = null;

    private static final boolean USE_IN_MEMORY_DATA_STORE = false;
}
