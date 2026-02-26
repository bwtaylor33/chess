package dataaccess;

public class DAOFactory {

    public static AuthTokenDAO getAuthTokenDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return new InMemoryAuthTokenDAO();
        }
        return null;
    }

    public static GameDAO getGameDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return new InMemoryGameDAO();
        }
        return null;
    }

    public static UserDAO getUserDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return new InMemoryUserDAO();
        }
        return null;
    }

    private static final boolean USE_IN_MEMORY_DATA_STORE = true;
}
