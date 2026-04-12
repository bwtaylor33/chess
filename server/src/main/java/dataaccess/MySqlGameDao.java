package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * MySQL game storage implementation
 */
public class MySqlGameDao extends MySqlBaseDao implements GameDao {

    public MySqlGameDao() throws DataAccessException {
        super(new String[] {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(64) DEFAULT NULL,
              `blackUsername` varchar(64) DEFAULT NULL,
              `gameName` varchar(64) NOT NULL,
              `game` varchar(4096) DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """});
    }

    public GameData createGame(String gameName) throws DataAccessException {

        var statement = "INSERT INTO game (gameName) VALUES (?)";
        int gameID = executeInsertReturnId(statement, gameName);

        if (gameID == 0) {
            throw new DataAccessException("Error creating game: " + gameName);
        }

        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        updateGame(gameData);

        return gameData;
    }

    public GameData getGame(int gameID) throws DataAccessException {

        try (ResultSet rs = getRecordByIntID("SELECT * FROM game WHERE gameID=?", gameID)) {

            if (rs == null) {
                System.out.println("got a null result set");
                throw new DataAccessException("Error: invalid gameID: " + gameID);
            }

            return readGame(rs);

        } catch(SQLException s) {
            throw new DataAccessException("Error: invalid gameID: " + gameID);
        }
    }

    public ArrayList<GameData> getAllGames() throws DataAccessException {

        try (ResultSet rs = getAllRecords("SELECT * FROM game ORDER BY gameID")) {

            var result = new ArrayList<GameData>();

            if (rs == null) {
                return result;
            }

            while (rs.next()) {
                result.add(readGame(rs));
            }

            return result;

        } catch(SQLException s) {
            throw new DataAccessException("Unable to get game list: " + s.getMessage());
        }
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        String json = null;

        if (gameData.getGame() != null) {
            json = new Gson().toJson(gameData.getGame());
        }

        int rowsUpdated = executeUpdate("UPDATE game SET whiteUsername=?, blackUsername=?, gameName=?, game=? WHERE gameID=?",
                gameData.getWhiteUsername(),
                gameData.getBlackUsername(),
                gameData.getGameName(),
                json,
                gameData.getGameID());

        if (rowsUpdated != 1) {
            throw new DataAccessException("Error: invalid gameID: " + gameData.getGameID());
        }
    }

    public void clearAllGames() throws DataAccessException {
        executeUpdate("TRUNCATE game");
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
