package dataaccess;

import model.UserData;

import java.util.HashMap;

public class InMemoryUserDAO implements UserDAO {

    public void createUser(UserData userData) throws DataAccessException {
        if (users.containsKey(userData.getUsername())) {
            throw new DataAccessException("Username already in use: " + userData.getUsername());
        }

        users.put(userData.getUsername(), userData);
        System.out.println("User added to database: " + userData.getUsername());
    }

    public UserData getUser(String username) throws DataAccessException {
        if (!users.containsKey(username)) {
            throw new DataAccessException("User not found: " + username);
        }

        return users.get(username);
    }

    public void updateUser(UserData userData) throws DataAccessException {
        if (!users.containsKey(userData.getUsername())) {
            throw new DataAccessException("User not found: " + userData.getUsername());
        }

        users.put(userData.getUsername(), userData);
    }

    public void deleteUser(String username) throws  DataAccessException {
        if (!users.containsKey(username)) {
            throw new DataAccessException("User not found: " + username);
        }

        users.remove(username);
    }

    public void clearAllUsers() throws DataAccessException {
        users = new HashMap<>();
    }

    private HashMap<String, UserData> users = new HashMap<>();
}
