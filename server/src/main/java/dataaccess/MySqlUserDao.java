package dataaccess;

import model.UserData;

import java.util.HashMap;

/**
 * In-memory user storage implementation
 */
public class MySqlUserDao extends MySqlBaseDao implements UserDao {

    public MySqlUserDao() {
        super([
            """
            CREATE TABLE IF NOT EXISTS  User (
              `username` varchar(64) NOT NULL,
              `password` varchar(64) NOT NULL,
              `email` varchar(64) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """]);
    }

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
