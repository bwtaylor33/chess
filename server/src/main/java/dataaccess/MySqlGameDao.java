package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * MySQL game storage implementation
 */
public class MySqlGameDao extends MySqlBaseDao implements GameDao {

    public MySqlGameDao() throws DataAccessException {
        super(new String[] {
            """
            CREATE TABLE IF NOT EXISTS  Game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(64) NOT NULL,
              `blackUsername` varchar(64) NOT NULL,
              `gameName` varchar(64) NOT NULL,
              `game` varchar(4096),
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """});
    }

    public GameData createGame(String gameName) throws DataAccessException {

        var statement = "INSERT INTO Game (gameName) VALUES (?)";
        int gameID = executeUpdate(statement, gameName);

        if (gameID == 0) {
            throw new DataAccessException("Error creating game: " + gameName);
        }

        return new GameData(gameID, null, null, gameName, new ChessGame());
    }

    public GameData getGame(int gameID) throws DataAccessException {

        try {
            ResultSet rs = getRecordByIntID("SELECT * FROM Game WHERE gameID=?", gameID);

            if (rs == null) {
                throw new DataAccessException("Error: invalid gameID: " + gameID);
            }

            return readGame(rs);

        } catch(SQLException s) {
            throw new DataAccessException("Error: invalid gameID: " + gameID);
        }
    }

    public ArrayList<GameData> getAllGames() throws DataAccessException {

        try {

            var result = new ArrayList<GameData>();
            ResultSet rs = getAllRecords("SELECT * FROM Game");

            if (rs == null) {
                throw new DataAccessException("Unable to get game list");
            }

            while (rs.next()) {
                result.add(readGame(rs));
            }

            return result;

        } catch(SQLException s) {
            throw new DataAccessException("Unable to get game list: " + s.getMessage());
        }
    }

    public void clearAllGames() throws DataAccessException {
        executeUpdate("TRUNCATE Game");
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");

        var json = rs.getString("game");
        ChessGame game = new Gson().fromJson(json, ChessGame.class);

        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
