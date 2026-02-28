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

        GameData gameData = new GameData(nextGameID++, null, null, gameName, new ChessGame());
        games.put(gameData.getGameID(), gameData);

        return gameData;
    }

    public GameData getGame(int gameID) throws DataAccessException {

        if (!games.containsKey(gameID)) {
            throw new DataAccessException("Error: game not found: " + gameID);
        }

        return games.get(gameID);
    }

    public ArrayList<GameData> getAllGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

//    public void updateGame(GameData gameData) throws DataAccessException {
//        games.put(gameData.getGameID(), gameData);
//    }

//    public void deleteGame(int gameID) throws  DataAccessException {
//
//        if (!games.containsKey(gameID)) {
//            throw new DataAccessException("Error: game not found: " + gameID);
//        }
//
//        games.remove(gameID);
//    }

    public void clearAllGames() throws DataAccessException {
        games.clear();
    }

    final private HashMap<Integer, GameData> games = new HashMap<>();
    private static int nextGameID = 1;
}
