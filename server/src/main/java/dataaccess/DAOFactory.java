package dataaccess;

public class DAOFactory {

    public static AuthTokenDAO getAuthTokenDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return new InMemoryAuthTokenDAO();
        }
        throw new RuntimeException("Sql access not implemented");
    }

    public static GameDAO getGameDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return new InMemoryGameDAO();
        }
        throw new RuntimeException("Sql access not implemented");
    }

    public static UserDAO getUserDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return new InMemoryUserDAO();
        }
        throw new RuntimeException("Sql access not implemented");
    }

    private static final boolean USE_IN_MEMORY_DATA_STORE = true;
}
