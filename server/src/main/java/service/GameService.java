package service;

import dataaccess.DAOFactory;
import dataaccess.DataAccessException;

public class GameService extends BaseService {

    public void clear() {
        try {
            DAOFactory.getGameDAO().clearAllGames();
        }catch (DataAccessException d) {
            throw new ResponseException("Error clearing database: " + d.getMessage());
        }
    }
}
