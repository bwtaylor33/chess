package dataaccess;

import model.GameData;

public interface GameDAO {

    public void createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public void updateGame(GameData gameData) throws DataAccessException;
    public void deleteGame(int gameID) throws  DataAccessException;
    public void clearAllGames() throws DataAccessException;
}
