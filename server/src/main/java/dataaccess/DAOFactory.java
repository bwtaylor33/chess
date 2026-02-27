package dataaccess;

public class DAOFactory {

    public static AuthTokenDAO getAuthTokenDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return authTokenDAO;
        }
        throw new RuntimeException("Sql access not implemented");
    }

    public static GameDAO getGameDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return gameDAO;
        }
        throw new RuntimeException("Sql access not implemented");
    }

    public static UserDAO getUserDAO() {
        if (USE_IN_MEMORY_DATA_STORE) {
            return userDAO;
        }
        throw new RuntimeException("Sql access not implemented");
    }

    private static final boolean USE_IN_MEMORY_DATA_STORE = true;
    private static final UserDAO userDAO = new InMemoryUserDAO();
    private static final GameDAO gameDAO = new InMemoryGameDAO();
    private static final AuthTokenDAO authTokenDAO = new InMemoryAuthTokenDAO();
}
