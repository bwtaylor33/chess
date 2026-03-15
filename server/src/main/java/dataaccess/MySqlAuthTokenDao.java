package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * MySQL authToken storage implementation
 */
public class MySqlAuthTokenDao extends MySqlBaseDao implements AuthTokenDao {

    public MySqlAuthTokenDao() throws DataAccessException {
        super(new String[] {
            """
            CREATE TABLE IF NOT EXISTS  AuthToken (
              `authToken` varchar(64) NOT NULL,
              `username` varchar(64) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """});
    }

    public void createAuthToken(AuthData authData) throws DataAccessException {

        var statement = "INSERT INTO AuthToken (authToken, username) VALUES (?, ?)";
        int id = executeUpdate(statement, authData.getAuthToken(), authData.getUsername());

        if (id == 0) {
            throw new DataAccessException("AuthToken already in use: " + authData.getAuthToken());
        }
    }

    public AuthData getAuthToken(String authToken) throws DataAccessException {

        try (ResultSet rs = getRecordByStringKey("SELECT * FROM AuthToken WHERE authToken=?", authToken)) {

            if (rs == null) {
                throw new DataAccessException("Error: invalid authToken: " + authToken);
            }

            return readAuthToken(rs);

        } catch(SQLException s) {
            throw new DataAccessException("Error: invalid authToken: " + authToken);
        }
    }

    public void deleteAuthToken(String authToken) throws DataAccessException {

        executeUpdate("DELETE FROM AuthToken WHERE authToken=?", authToken);
        // silently fail if AuthToken doesn't exist
    }

    public void clearAllAuthTokens() throws DataAccessException {
        executeUpdate("TRUNCATE AuthToken");
    }

    private AuthData readAuthToken(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }
}
