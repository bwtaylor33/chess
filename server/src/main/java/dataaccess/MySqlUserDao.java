package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * MySQL user storage implementation
 */
public class MySqlUserDao extends MySqlBaseDao implements UserDao {

    public MySqlUserDao() throws DataAccessException {
        super(new String[] {
            """
            CREATE TABLE IF NOT EXISTS  User (
              `username` varchar(64) NOT NULL,
              `password` varchar(64) NOT NULL,
              `email` varchar(64) NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """});
    }

    public void createUser(UserData userData) throws DataAccessException {

        var statement = "INSERT INTO User (username, password, email) VALUES (?, ?, ?)";
        int id = executeUpdate(statement, userData.getUsername(), userData.getPassword(), userData.getEmail());

        if (id == 0) {
            throw new DataAccessException("Username already in use: " + userData.getUsername());
        }
    }

    public UserData getUser(String username) throws DataAccessException {

        try {
            return readUser(getRecordByStringKey("SELECT * FROM User WHERE username=?", username));

        } catch(SQLException s) {
            throw new DataAccessException("Error: invalid username: " + username);
        }
    }

    public void clearAllUsers() throws DataAccessException {
        executeUpdate("TRUNCATE User");
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
    }
}
