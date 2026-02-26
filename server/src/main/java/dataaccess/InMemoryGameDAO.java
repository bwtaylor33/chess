package dataaccess;

import model.GameData;

import java.util.HashMap;

public class InMemoryGameDAO implements GameDAO {

    public void createGame(GameData gameData) throws DataAccessException {
        if (!games.containsKey(gameData.getGameID())) {
            throw new DataAccessException("Game already exists for given gameID: " + gameData.getGameID());
        }

        games.put(gameData.getGameID(), gameData);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        if (!games.containsKey(gameID)) {
            throw new DataAccessException("Game not found: " + gameID);
        }

        return games.get(gameID);
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        games.put(gameData.getGameID(), gameData);
    }

    public void deleteGame(int gameID) throws  DataAccessException {
        if (!games.containsKey(gameID)) {
            throw new DataAccessException("Game not found: " + gameID);
        }

        games.remove(gameID);
    }

    public void clearAllGames() throws DataAccessException {
        games = new HashMap<>();
    }

    private HashMap<Integer, GameData> games = new HashMap<>();
}
