package dataaccess;

import service.ResponseException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlBaseDao {

    public MySqlBaseDao(String[] createStatements) {
        this.createStatements = createStatements;
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: " + ex.getMessage());
        }
    }

    private final String[] createStatements;
}
