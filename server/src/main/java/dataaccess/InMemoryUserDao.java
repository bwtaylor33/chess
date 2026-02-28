package dataaccess;

import model.UserData;
import java.util.HashMap;

/**
 * In-memory user storage implementation
 */
public class InMemoryUserDao implements UserDao {

    public void createUser(UserData userData) throws DataAccessException {

        if (users.containsKey(userData.getUsername())) {
            throw new DataAccessException("Username already in use: " + userData.getUsername());
        }

        users.put(userData.getUsername(), userData);
    }

    public UserData getUser(String username) throws DataAccessException {

        if (!users.containsKey(username)) {
            throw new DataAccessException("Error: invalid username: " + username);
        }

        return users.get(username);
    }

    public void clearAllUsers() throws DataAccessException {
        users.clear();
    }

    final private HashMap<String, UserData> users = new HashMap<>();
}
