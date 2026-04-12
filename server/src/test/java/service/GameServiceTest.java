package service;

import chess.ChessGame;
import dataaccess.DaoFactory;
import dataaccess.DataAccessException;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameServiceTest {

    @BeforeEach
    public void setUp() throws ResponseException {

        gameService = new GameService();
        gameService.clear();

        userService = new UserService();
        userService.clear();

        authToken = userService.register(new RegisterRequest("testUsername", "testPassword", "test@junk.com")).authToken();
    }

    @Test
    public void testCreateGameSuccess() throws ResponseException {

        int gameID = gameService.createGame(authToken, new CreateGameRequest("testGameName")).gameID();
        Assertions.assertTrue(gameID >= 1);
    }

    @Test
    public void testCreateGameFailure() {

        // trying to create with blank game name
        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            gameService.createGame(authToken, new CreateGameRequest(""));
        });
        Assertions.assertEquals("Error: gameName cannot be empty", exception.getMessage());
    }

    @Test
    public void testJoinGameSuccess() throws DataAccessException, ResponseException {

        // create game and capture gameID
        int gameID = gameService.createGame(authToken, new CreateGameRequest("testGameName")).gameID();
        gameService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID));

        // confirm that game is in database
        Assertions.assertEquals("testGameName", DaoFactory.getGameDao().getGame(gameID).getGameName());
    }

    @Test
    public void testJoinGameFailure() throws ResponseException {

        // test for invalid gameID
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, 999));
        });
        Assertions.assertEquals("Error loading game: Error: Invalid gameID: 999", exception.getMessage());

        // test for joining overtop a player
        int gameID = gameService.createGame(authToken, new CreateGameRequest("testGameName")).gameID();
        gameService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID));

        exception = Assertions.assertThrows(ForbiddenRequestException.class, () -> {
            gameService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID));
        });
        Assertions.assertEquals("Error: BLACK player already taken", exception.getMessage());
    }

    @Test
    public void testListGamesSuccess() throws ResponseException {

        // create 2 games
        gameService.createGame(authToken, new CreateGameRequest("testGameName"));
        gameService.createGame(authToken, new CreateGameRequest("testGameName1"));

        // confirm that the 2 games were made
        ArrayList<GameData> games = gameService.listGames(authToken).games();
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void testListGamesFailure() throws ResponseException {

        userService.logout(authToken);

        // test for unauthenticated call to listGames
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.listGames(authToken);
        });
        Assertions.assertEquals("Error: Invalid authToken: " + authToken, exception.getMessage());
    }

    @Test
    public void testClear() throws ResponseException {

        // creating 2 games
        gameService.createGame(authToken, new CreateGameRequest("testGameName"));
        gameService.createGame(authToken, new CreateGameRequest("testGameName1"));

        // confirming that 2 games return back from listGames
        ArrayList<GameData> games = gameService.listGames(authToken).games();
        Assertions.assertEquals(2, games.size());

        gameService.clear();

        // confirm games list is now empty
        games = gameService.listGames(authToken).games();
        Assertions.assertEquals(0, games.size());
    }

    private GameService gameService;
    private UserService userService;
    private String authToken;
}
