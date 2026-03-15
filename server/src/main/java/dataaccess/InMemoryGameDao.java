package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * In-memory game storage implementation
 */
public class InMemoryGameDao implements GameDao {

    public GameData createGame(String gameName) throws DataAccessException {

        if (gameName == null) {
            throw new DataAccessException("unable to update database: Column 'gameName' cannot be null");
        }

        GameData gameData = new GameData(nextGameID++, null, null, gameName, new ChessGame());
        games.put(gameData.getGameID(), gameData);

        return gameData;
    }

    public GameData getGame(int gameID) throws DataAccessException {

        if (!games.containsKey(gameID)) {
            throw new DataAccessException("Error: invalid gameID: " + gameID);
        }

        return games.get(gameID);
    }

    public void updateGame(GameData gameData) throws DataAccessException {

        if (!games.containsKey(gameData.getGameID())) {
            throw new DataAccessException("Error: invalid gameID: " + gameData.getGameID());
        }

        games.put(gameData.getGameID(), gameData);
    }

    public ArrayList<GameData> getAllGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

    public void clearAllGames() throws DataAccessException {
        games.clear();
    }

    final private HashMap<Integer, GameData> games = new HashMap<>();
    private static int nextGameID = 1;
}
