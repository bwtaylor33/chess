package dataaccess;

import model.GameData;

public interface GameDAO {

    public GameData createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public void updateGame(GameData gameData) throws DataAccessException;
    public void deleteGame(int gameID) throws  DataAccessException;
    public void clearAllGames() throws DataAccessException;
}
