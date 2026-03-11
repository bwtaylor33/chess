package dataaccess;

import chess.ChessGame;
import service.ResponseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlBaseDao {

    public MySqlBaseDao(String[] createStatements) throws DataAccessException {
        this.createStatements = createStatements;
        configureDatabase();
    }

    protected int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to update database: " + e.getMessage());
        }
    }

    protected ResultSet getRecordByStringKey(String query, String key) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, key);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }

    protected ResultSet getRecordByIntID(String query, int id) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
    }

    protected ResultSet getAllRecords(String query) throws DataAccessException {

        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data: " + e.getMessage());
        }
        return null;
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
