package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;

/**
 * Service handles all game functions
 */
public class GameService extends BaseService {

    public CreateGameResult createGame(String authToken, CreateGameRequest createGameRequest) throws ResponseException {

        validateAuthToken(authToken);

        if (createGameRequest.gameName() == null || createGameRequest.gameName().isBlank()) {
            throw new BadRequestException("Error: gameName cannot be empty");
        }

        try{
            GameDao gameDao = DaoFactory.getGameDao();

            // create game in game table
            GameData gameData = gameDao.createGame(createGameRequest.gameName());

            // return the authToken
            return new CreateGameResult(gameData.getGameID());

        }catch (DataAccessException e) {
            throw new ResponseException("Error creating user: " + e.getMessage());
        }
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ResponseException {

        try {
            validateAuthToken(authToken);

        } catch (ForbiddenRequestException f) {
            throw new ResponseException(f.getMessage());
        }

        if (joinGameRequest.gameID() <= 0) {
            throw new BadRequestException("Error: invalid gameID: " + joinGameRequest.gameID());
        }

        try{
            AuthTokenDao authTokenDao = DaoFactory.getAuthTokenDao();
            GameDao gameDao = DaoFactory.getGameDao();

            // get username from authToken
            String username = authTokenDao.getAuthToken(authToken).getUsername();

            // join game in game table
            GameData gameData = gameDao.getGame(joinGameRequest.gameID());

            if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE) {

                if (gameData.getWhiteUsername() != null) {
                    throw new ForbiddenRequestException("Error: white player already taken");
                }

                gameData.setWhiteUsername(username);

            }else if (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK) {

                if (gameData.getBlackUsername() != null) {
                    throw new ForbiddenRequestException("Error: black player already taken");
                }

                gameData.setBlackUsername(username);

            }else {
                throw new BadRequestException("Error: invalid team color: " + joinGameRequest.playerColor());
            }

            gameDao.updateGame(gameData);

        }catch (DataAccessException e) {
            throw new ResponseException("Error joining game: " + e.getMessage());
        }
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {

        validateAuthToken(authToken);

        try{
            GameDao gameDao = DaoFactory.getGameDao();

            // get all games in game table
            return new ListGamesResult(gameDao.getAllGames());

        }catch (DataAccessException e) {
            throw new ResponseException("Error getting games list: " + e.getMessage());
        }
    }

    public void clear() throws ResponseException {

        try {
            DaoFactory.getGameDao().clearAllGames();

        }catch (DataAccessException d) {
            throw new ResponseException("Error clearing database: " + d.getMessage());
        }
    }
}