package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

/**
 * In-memory user storage implementation
 */
public class InMemoryUserDao implements UserDao {

    public void createUser(UserData userData) throws DataAccessException {

        if (users.containsKey(userData.getUsername())) {
            throw new DataAccessException("unable to update database: Duplicate entry '" + userData.getUsername() + "' for key 'user.PRIMARY'");
        }

        userData.setPassword(BCrypt.hashpw(userData.getPassword(), BCrypt.gensalt()));

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
