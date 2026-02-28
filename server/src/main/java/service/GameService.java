package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.response.CreateGameResult;
import model.response.ListGamesResult;

/**
 * Service handles all game functions
 */
public class GameService extends BaseService {

    public CreateGameResult createGame(String authToken, String gameName) throws ResponseException {

        validateAuthToken(authToken);

        if (gameName == null || gameName.isBlank()) {
            throw new BadRequestException("Error: gameName cannot be empty");
        }

        GameDao gameDao = DaoFactory.getGameDao();

        try{
            // create game in game table
            GameData gameData = gameDao.createGame(gameName);

            // return the authToken
            return new CreateGameResult(gameData.getGameID());

        }catch (DataAccessException e) {
            throw new ResponseException("Error creating user: " + e.getMessage());
        }
    }

    public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws ResponseException {

        validateAuthToken(authToken);

        if (gameID <= 0) {
            throw new BadRequestException("Error: invalid gameID: " + gameID);
        }

        AuthTokenDao authTokenDao = DaoFactory.getAuthTokenDao();
        GameDao gameDao = DaoFactory.getGameDao();

        try{
            // get username from authToken
            String username = authTokenDao.getAuthToken(authToken).getUsername();

            // join game in game table
            GameData gameData = gameDao.getGame(gameID);

            if (playerColor == ChessGame.TeamColor.WHITE) {

                if (gameData.getWhiteUsername() != null) {
                    throw new ForbiddenRequestException("Error: white player already taken");
                }

                gameData.setWhiteUsername(username);

            }else if (playerColor == ChessGame.TeamColor.BLACK) {

                if (gameData.getBlackUsername() != null) {
                    throw new ForbiddenRequestException("Error: black player already taken");
                }

                gameData.setBlackUsername(username);

            }else {
                throw new BadRequestException("Error: invalid team color: " + playerColor);
            }

        }catch (DataAccessException e) {
            throw new ResponseException("Error joining game: " + e.getMessage());
        }
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {

        validateAuthToken(authToken);

        GameDao gameDao = DaoFactory.getGameDao();

        try{
            // get all games in game table
            return new ListGamesResult(gameDao.getAllGames());

        }catch (DataAccessException e) {
            throw new ResponseException("Error getting games list: " + e.getMessage());
        }
    }

    public void clear() {

        try {
            DaoFactory.getGameDao().clearAllGames();

        }catch (DataAccessException d) {
            throw new ResponseException("Error clearing database: " + d.getMessage());
        }
    }
}