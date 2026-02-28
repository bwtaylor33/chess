package dataaccess;

import model.UserData;

/**
 * User DAO interface
 */
public interface UserDao {

    public void createUser(UserData userData) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
//    public void updateUser(UserData userData) throws DataAccessException;
//    public void deleteUser(String username) throws  DataAccessException;
    public void clearAllUsers() throws DataAccessException;
}
