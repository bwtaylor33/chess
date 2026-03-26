package client;

import chess.ChessGame;
import dataaccess.DaoFactory;
import dataaccess.DataAccessException;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;
import model.response.LoginResult;
import model.response.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import service.BadRequestException;
import service.ResponseException;

import javax.xml.crypto.Data;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade client;

    @BeforeAll
    public static void init() {

        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        client = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        DaoFactory.getAuthTokenDao().clearAllAuthTokens();
        DaoFactory.getUserDao().clearAllUsers();
        DaoFactory.getGameDao().clearAllGames();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testRegisterSuccess() throws ResponseException {

        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        Assertions.assertEquals("testUser", registerResult.username());
        Assertions.assertNotNull(registerResult.authToken());
    }

    @Test
    public void testRegisterFailure() {

        // trying to register duplicate user
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
            client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        });
        Assertions.assertEquals("Error: username is already in use: testUser", exception.getMessage());
    }

    @Test
    public void testLoginSuccess() throws ResponseException {

        // register new user and logout
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        client.logout(registerResult.authToken());

        // test login
        LoginResult loginResult = client.login(new LoginRequest("testUser", "testPassword"));
        Assertions.assertEquals("testUser", loginResult.username());
        Assertions.assertNotNull(loginResult.authToken());
    }

    @Test
    public void testLoginFailure() throws ResponseException {

        // register new user and logout
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        client.logout(registerResult.authToken());

        // trying to register duplicate user
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            client.login(new LoginRequest("testUser", "badPassword"));
        });
        Assertions.assertEquals("Error logging in user: Error: incorrect password", exception.getMessage());
    }

    @Test
    public void testLogoutSuccess() throws ResponseException {

        // register new user and logout
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        client.logout(registerResult.authToken());

        // try to do something that requires valid authToken
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            client.listGames(registerResult.authToken());
        });
        Assertions.assertTrue(exception.getMessage().startsWith("Error: Error: invalid authToken: "));
    }

    @Test
    public void testLogoutFailure() throws ResponseException {

        // try to pass invalid authToken
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            client.logout("badToken");
        });
        Assertions.assertTrue(exception.getMessage().startsWith("Error: Error: invalid authToken: "));
    }

    @Test
    public void testCreateGameSuccess() throws ResponseException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameResult createGameResult = client.createGame(registerResult.authToken(), new CreateGameRequest("testGameName"));
        Assertions.assertTrue(createGameResult.gameID() > 0);
    }

    @Test
    public void testCreateGameFailure() throws ResponseException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        // trying to create with blank game name
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            client.createGame(registerResult.authToken(), new CreateGameRequest(""));
        });
        Assertions.assertEquals("Error: gameName cannot be empty", exception.getMessage());
    }

    @Test
    public void testJoinGameSuccess() throws ResponseException, DataAccessException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameResult createGameResult = client.createGame(registerResult.authToken(), new CreateGameRequest("testGameName"));
        client.joinGame(registerResult.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, createGameResult.gameID()));

        // confirm that game is in database
        Assertions.assertEquals("testGameName", DaoFactory.getGameDao().getGame(createGameResult.gameID()).getGameName());
    }

    @Test
    public void testJoinGameFailure() throws ResponseException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        // test for invalid gameID
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            client.joinGame(registerResult.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, 999));
        });
        Assertions.assertEquals("Error joining game: Error: invalid gameID: 999", exception.getMessage());
    }

    @Test
    public void testListGamesSuccess() throws ResponseException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        // create 2 games
        client.createGame(registerResult.authToken(), new CreateGameRequest("testGameName"));
        client.createGame(registerResult.authToken(), new CreateGameRequest("testGameName2"));

        // confirm there are two games listed
        ListGamesResult listGamesResult = client.listGames(registerResult.authToken());
        Assertions.assertEquals(2, listGamesResult.games().size());
    }

    @Test
    public void testListGamesFailure() throws ResponseException {

        // test for unauthenticated call to listGames
        Exception exception = Assertions.assertThrows(ResponseException.class, () -> {
            client.listGames("badToken");
        });
        Assertions.assertEquals("Error: Error: invalid authToken: badToken", exception.getMessage());
    }
}
