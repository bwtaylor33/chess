package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.request.RegisterRequest;
import model.response.CreateGameResult;
import model.response.RegisterResult;

public class GameService extends BaseService {

    public CreateGameResult createGame(String authToken, String gameName) throws ResponseException {

        validateAuthToken(authToken);

        if (gameName == null || gameName.isBlank()) {
            throw new BadRequestException("Error: invalid gameName: " + gameName);
        }

        GameDAO gameDAO = DAOFactory.getGameDAO();

        try{
            // create game in game table
            GameData gameData = gameDAO.createGame(gameName);

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

        AuthTokenDAO authTokenDAO = DAOFactory.getAuthTokenDAO();
        GameDAO gameDAO = DAOFactory.getGameDAO();

        try{
            // get username from authToken
            String username = authTokenDAO.getAuthToken(authToken).getUsername();

            // join game in game table
            GameData gameData = gameDAO.getGame(gameID);
            if (playerColor == ChessGame.TeamColor.WHITE) {
                gameData.setWhiteUsername(username);
            }
            if (playerColor == ChessGame.TeamColor.BLACK) {
                gameData.setBlackUsername(username);
            }else {
                throw new BadRequestException("Error: invalid team color: " + playerColor);
            }

        }catch (DataAccessException e) {
            throw new ResponseException("Error joining game: " + e.getMessage());
        }
    }

    public void clear() {
        try {
            DAOFactory.getGameDAO().clearAllGames();
        }catch (DataAccessException d) {
            throw new ResponseException("Error clearing database: " + d.getMessage());
        }
    }
}
