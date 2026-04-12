package service;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.*;
import model.GameData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.CreateGameResult;
import model.response.ListGamesResult;
import model.response.MakeMoveResult;
import websocket.commands.LeaveGameCommand;
import websocket.commands.ResignGameCommand;
import websocket.commands.MakeMoveCommand;

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

    public GameData getGame(String authToken, int gameID) throws ResponseException {

        validateAuthToken(authToken);

        if (gameID <= 0) {
            throw new BadRequestException("Error: Invalid gameID: " + gameID);
        }

        try{
            // retrieve game from the database
            return DaoFactory.getGameDao().getGame(gameID);

        }catch (DataAccessException e) {
            throw new ResponseException("Error loading game: " + e.getMessage());
        }
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ResponseException {

        // get game from database (and validate authToken)
        GameData gameData = getGame(authToken, joinGameRequest.gameID());

        try {
            // get username from authToken
            String username = DaoFactory.getAuthTokenDao().getAuthToken(authToken).getUsername();

            if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE) {

                if (gameData.getWhiteUsername() != null) {
                    throw new ForbiddenRequestException("Error: WHITE player already taken");
                }

                gameData.setWhiteUsername(username);

            }else if (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK) {

                if (gameData.getBlackUsername() != null) {
                    throw new ForbiddenRequestException("Error: BLACK player already taken");
                }

                gameData.setBlackUsername(username);

            }else {
                throw new BadRequestException("Error: Invalid team color: " + joinGameRequest.playerColor());
            }

            DaoFactory.getGameDao().updateGame(gameData);

        }catch (DataAccessException e) {
            throw new ResponseException("Error joining game: " + e.getMessage());
        }
    }

    public void leaveGame(String authToken, LeaveGameCommand leaveGameCommand) throws ResponseException {

        String username = validateAuthToken(authToken);

        if (leaveGameCommand.getGameID() <= 0) {
            throw new BadRequestException("Error: Invalid gameID: " + leaveGameCommand.getGameID());
        }

        try{
            GameDao gameDao = DaoFactory.getGameDao();

            // vacate spot for the game in game table
            GameData gameData = gameDao.getGame(leaveGameCommand.getGameID());

            boolean leftAsWhite = false;
            boolean leftAsBlack = false;

            if (gameData.getWhiteUsername() != null && gameData.getWhiteUsername().equals(username)) {
                gameData.setWhiteUsername(null);
                leftAsWhite = true;
            }

            if (gameData.getBlackUsername() != null && gameData.getBlackUsername().equals(username)) {
                gameData.setBlackUsername(null);
                leftAsBlack = true;
            }

            if(!leftAsWhite && !leftAsBlack){
                throw new BadRequestException("Error leaving game: " + username + " was not a participant");
            }

            gameDao.updateGame(gameData);

        }catch (DataAccessException e) {
            throw new ResponseException("Error leaving game: " + e.getMessage());
        }
    }

    public void resignGame(String authToken, ResignGameCommand resignGameCommand) throws ResponseException {

        String username = validateAuthToken(authToken);

        if (resignGameCommand.getGameID() <= 0) {
            throw new BadRequestException("Error: Invalid gameID: " + resignGameCommand.getGameID());
        }

        try{
            GameDao gameDao = DaoFactory.getGameDao();

            // vacate spot for the game in game table
            GameData gameData = gameDao.getGame(resignGameCommand.getGameID());

            boolean resignedAsWhite = false;
            boolean resignedAsBlack = false;

            if (gameData.getWhiteUsername().equals(username)) {
                gameData.getGame().setGameOver(ChessGame.TeamColor.BLACK);
                resignedAsWhite = true;
            }

            if (gameData.getBlackUsername().equals(username)) {
                gameData.getGame().setGameOver(ChessGame.TeamColor.WHITE);
                resignedAsBlack = true;
            }

            if(!resignedAsWhite && !resignedAsBlack){
                throw new BadRequestException("Error resigning game: " + username + " was not a participant");
            }

            gameDao.updateGame(gameData);

        }catch (DataAccessException e) {
            throw new ResponseException("Error leaving game: " + e.getMessage());
        }
    }

    public MakeMoveResult makeMove(String authToken, MakeMoveCommand makeMoveCommand) throws ResponseException {

        // get game from database (and validate authToken)
        GameData gameData = getGame(makeMoveCommand.getAuthToken(), makeMoveCommand.getGameID());

        try {
            // get username from authToken
            String username = DaoFactory.getAuthTokenDao().getAuthToken(authToken).getUsername();

            // determine color for username (null when observing)

            ChessGame.TeamColor color = null;

            if(gameData.getWhiteUsername() != null && gameData.getWhiteUsername().equals(username)){
                color = ChessGame.TeamColor.WHITE;

            }else if(gameData.getBlackUsername() != null && gameData.getBlackUsername().equals(username)){
                color = ChessGame.TeamColor.BLACK;
            }

            // prevent observers from trying to move
            if(color == null){
                throw new ResponseException("Move prohibited.  You are only an observer on this game.");
            }

            ChessGame.TeamColor opponentColor = color == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

            // test to see if game is already over
            if(gameData.getGame().isGameOver()){
                throw new ResponseException("Move prohibited: Game is over.");
            }

            // make sure it is player's turn
            if(gameData.getGame().getTeamTurn() != color){
                throw new ResponseException("Move prohibited: Please wait for your turn.");
            }

            // make the move
            try {
                gameData.getGame().makeMove(makeMoveCommand.getMove());

            }catch(InvalidMoveException x){
                throw new ResponseException("Move prohibited: Invalid move: " + x.getMessage());
            }

            String specialMessage = null;

            // game status checks

            if(gameData.getGame().isInCheckmate(opponentColor)){
                gameData.getGame().setGameOver(color);
                specialMessage = String.format("Checkmate! %s has won the game!", color);

            }else if(gameData.getGame().isInStalemate(opponentColor)){
                gameData.getGame().setGameOver(null);
                specialMessage = String.format("Stalemate: The game ends in a tie.");

            }else if(gameData.getGame().isInCheck(opponentColor)){
                specialMessage = String.format("Check! A move by %s has put %s in danger!", color, opponentColor);
            }

            // update the game
            DaoFactory.getGameDao().updateGame(gameData);

            return new MakeMoveResult(String.format("%s has made a move.", color), specialMessage);

        }catch(DataAccessException e){
            throw new ResponseException("Error executing move: " + e.getMessage());
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