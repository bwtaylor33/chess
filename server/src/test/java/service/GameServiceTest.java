package service;

import chess.ChessGame;
import model.GameData;
import model.request.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameServiceTest {

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
        gameService.clear();

        userService = new UserService();
        userService.clear();

        authToken = userService.register(new RegisterRequest("testUsername", "testPassword", "test@junk.com")).authToken();
    }

    @Test
    public void testCreateGameSuccess() {
        int gameID = gameService.createGame(authToken, "testGameName").gameID();
        Assertions.assertTrue(gameID >= 1);
    }

    @Test
    public void testCreateGameFailure() {
        Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
            gameService.createGame(authToken, "");
        });
        Assertions.assertEquals("Error: gameName cannot be empty", exception.getMessage());
    }

    @Test
    public void testJoinGameSuccess() {
        int gameID = gameService.createGame(authToken, "testGameName").gameID();
        gameService.joinGame(authToken, ChessGame.TeamColor.BLACK, gameID);
    }

    @Test
    public void testJoinGameFailure() {
        // test for invalid gameID
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.joinGame(authToken, ChessGame.TeamColor.BLACK, 999);
        });
        Assertions.assertEquals("Error joining game: Error: game not found: 999", exception.getMessage());

        // test for joining overtop a player
        int gameID = gameService.createGame(authToken, "testGameName").gameID();
        gameService.joinGame(authToken, ChessGame.TeamColor.BLACK, gameID);

        exception = Assertions.assertThrows(ForbiddenRequestException.class, () -> {
            gameService.joinGame(authToken, ChessGame.TeamColor.BLACK, gameID);
        });
        Assertions.assertEquals("Error: black player already taken", exception.getMessage());
    }

    @Test
    public void testListGamesSuccess() {
        gameService.createGame(authToken, "testGameName");
        gameService.createGame(authToken, "testGameName1");
        ArrayList<GameData> games = gameService.listGames(authToken).games();
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void testListGamesFailure() {
        userService.logout(authToken);

        // test for unauthenticated call to listGames
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            gameService.listGames(authToken);
        });
        Assertions.assertEquals("Error: invalid authToken: " + authToken, exception.getMessage());
    }

    @Test
    public void testClear() {
        gameService.createGame(authToken, "testGameName");
        gameService.createGame(authToken, "testGameName1");
        ArrayList<GameData> games = gameService.listGames(authToken).games();
        Assertions.assertEquals(2, games.size());
        gameService.clear();
        games = gameService.listGames(authToken).games();
        Assertions.assertEquals(0, games.size());
    }

    private GameService gameService;
    private UserService userService;
    private String authToken;
}
