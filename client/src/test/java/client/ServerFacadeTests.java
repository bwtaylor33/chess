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
    public void testRegisterSuccess() throws ClientException {

        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        Assertions.assertEquals("testUser", registerResult.username());
        Assertions.assertNotNull(registerResult.authToken());
    }

    @Test
    public void testRegisterFailure() {

        // trying to register duplicate user
        Exception exception = Assertions.assertThrows(ClientException.class, () -> {
            client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
            client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        });
        Assertions.assertEquals("Error: username is already in use: testUser", exception.getMessage());
    }

    @Test
    public void testLoginSuccess() throws ClientException {

        // register new user and logout
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        client.logout(registerResult.authToken());

        // test login
        LoginResult loginResult = client.login(new LoginRequest("testUser", "testPassword"));
        Assertions.assertEquals("testUser", loginResult.username());
        Assertions.assertNotNull(loginResult.authToken());
    }

    @Test
    public void testLoginFailure() throws ClientException {

        // register new user and logout
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        client.logout(registerResult.authToken());

        // trying to register duplicate user
        Exception exception = Assertions.assertThrows(ClientException.class, () -> {
            client.login(new LoginRequest("testUser", "badPassword"));
        });
        Assertions.assertEquals("Error: Incorrect password", exception.getMessage());
    }

    @Test
    public void testLogoutSuccess() throws ClientException {

        // register new user and logout
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));
        client.logout(registerResult.authToken());

        // try to do something that requires valid authToken
        Exception exception = Assertions.assertThrows(ClientException.class, () -> {
            client.listGames(registerResult.authToken());
        });
        Assertions.assertTrue(exception.getMessage().startsWith("Error: Invalid authToken: "));
    }

    @Test
    public void testLogoutFailure() throws ClientException {

        // try to pass invalid authToken
        Exception exception = Assertions.assertThrows(ClientException.class, () -> {
            client.logout("badToken");
        });
        Assertions.assertTrue(exception.getMessage().startsWith("Error: Invalid authToken: "));
    }

    @Test
    public void testCreateGameSuccess() throws ClientException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameResult createGameResult = client.createGame(registerResult.authToken(), new CreateGameRequest("testGameName"));
        Assertions.assertTrue(createGameResult.gameID() > 0);
    }

    @Test
    public void testCreateGameFailure() throws ClientException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        // trying to create with blank game name
        Exception exception = Assertions.assertThrows(ClientException.class, () -> {
            client.createGame(registerResult.authToken(), new CreateGameRequest(""));
        });
        Assertions.assertEquals("Error: gameName cannot be empty", exception.getMessage());
    }

    @Test
    public void testJoinGameSuccess() throws ClientException, DataAccessException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        CreateGameResult createGameResult = client.createGame(registerResult.authToken(), new CreateGameRequest("testGameName"));
        client.joinGame(registerResult.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, createGameResult.gameID()));

        // confirm that game is in database
        Assertions.assertEquals("testGameName", DaoFactory.getGameDao().getGame(createGameResult.gameID()).getGameName());
    }

    @Test
    public void testJoinGameFailure() throws ClientException {

        // register new user
        RegisterResult registerResult = client.register(new RegisterRequest("testUser", "testPassword", "testEmail"));

        // test for invalid gameID
        Exception exception = Assertions.assertThrows(ClientException.class, () -> {
            client.joinGame(registerResult.authToken(), new JoinGameRequest(ChessGame.TeamColor.BLACK, 999));
        });
        Assertions.assertEquals("Error loading game: Error: Invalid gameID: 999", exception.getMessage());
    }

    @Test
    public void testListGamesSuccess() throws ClientException {

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
    public void testListGamesFailure() throws ClientException {

        // test for unauthenticated call to listGames
        Exception exception = Assertions.assertThrows(ClientException.class, () -> {
            client.listGames("badToken");
        });
        Assertions.assertEquals("Error: Invalid authToken: badToken", exception.getMessage());
    }
}
