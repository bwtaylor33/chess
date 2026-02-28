package dataaccess;

import model.GameData;
import java.util.ArrayList;

/**
 * Game DAO interface
 */
public interface GameDao {

    public GameData createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public ArrayList<GameData> getAllGames() throws DataAccessException;
//    public void updateGame(GameData gameData) throws DataAccessException;
//    public void deleteGame(int gameID) throws  DataAccessException;
    public void clearAllGames() throws DataAccessException;
}
