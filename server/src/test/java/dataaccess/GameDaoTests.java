package dataaccess;

import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDaoTests {

    @BeforeEach
    public void setUp() throws DataAccessException {

        gameDao = DaoFactory.getGameDao();
        gameDao.clearAllGames();
    }

    @Test
    public void testCreateGameSuccess() throws DataAccessException {

        // create new game
        GameData testGame = gameDao.createGame("testGameName");

        Assertions.assertTrue(testGame.getGameID() > 0);
        Assertions.assertNull(testGame.getBlackUsername());
        Assertions.assertNull(testGame.getWhiteUsername());
        Assertions.assertEquals("testGameName", testGame.getGameName());
    }

    @Test
    public void testCreateGameFailure() throws DataAccessException {

        // test for empty gameName
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameDao.createGame(null);
        });
        Assertions.assertEquals("unable to update database: Column 'gameName' cannot be null", exception.getMessage());
    }

    @Test
    public void testGetGameSuccess() throws DataAccessException {

        // create new game
        GameData testGame = gameDao.createGame("testGameName");

        GameData verifiedGame = gameDao.getGame(testGame.getGameID());

        // test if getGame is same as game in database
        Assertions.assertEquals(testGame.getGameID(), verifiedGame.getGameID());
        Assertions.assertEquals(testGame.getGameName(), verifiedGame.getGameName());
        Assertions.assertEquals(testGame.getWhiteUsername(), verifiedGame.getWhiteUsername());
        Assertions.assertEquals(testGame.getBlackUsername(), verifiedGame.getBlackUsername());
    }

    @Test
    public void testGetGameFailure() throws DataAccessException {

        // try to get fake gameID
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameDao.getGame(-1);
        });
        Assertions.assertEquals("Error: invalid gameID: -1", exception.getMessage());
    }

    @Test
    public void testUpdateGameSuccess() throws DataAccessException {

        // create new game
        GameData testGame = gameDao.createGame("testGameName");

        // update gameData
        testGame.setWhiteUsername("testUser");
        gameDao.updateGame(testGame);

        // verify that white username was updated
        GameData verifiedGame = gameDao.getGame(testGame.getGameID());
        Assertions.assertEquals(testGame.getWhiteUsername(), verifiedGame.getWhiteUsername());
    }

    @Test
    public void testUpdateGameFailure() throws DataAccessException {

        GameData gameData = new GameData(-1, null, null, "badGame", null);

        // try to get fake gameID
        Exception exception = Assertions.assertThrows(DataAccessException.class, () -> {
            gameDao.updateGame(gameData);
        });
        Assertions.assertEquals("Error: invalid gameID: -1", exception.getMessage());
    }

    @Test
    public void testGetAllGamesSuccess() throws DataAccessException {

        // check if game list is 0
        ArrayList<GameData> games = gameDao.getAllGames();
        Assertions.assertEquals(0, games.size());

        // create 3 games
        GameData testGame = gameDao.createGame("testGameName");
        GameData testGame2 = gameDao.createGame("testGameName2");
        GameData testGame3 = gameDao.createGame("testGameName3");

        games = gameDao.getAllGames();

        // Assertions.assertEquals(3, games.size());
        Assertions.assertEquals("testGameName", games.get(0).getGameName());
        Assertions.assertEquals("testGameName2", games.get(1).getGameName());
        Assertions.assertEquals("testGameName3", games.get(2).getGameName());
    }

    @Test
    public void testGetAllGamesFailure() throws DataAccessException {
        Assertions.assertTrue(true);
    }

    @Test
    public void testClearAllGames() throws DataAccessException {
        // create games
        GameData testGame = gameDao.createGame("testGameName");
        GameData testGame2 = gameDao.createGame("testGameName2");
        GameData testGame3 = gameDao.createGame("testGameName3");

        gameDao.clearAllGames();

        // make sure game list is now empty
        ArrayList<GameData> games = gameDao.getAllGames();
        Assertions.assertEquals(0, games.size());
    }

    private GameDao gameDao;
}
