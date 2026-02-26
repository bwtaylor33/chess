package dataaccess;

import model.UserData;

public interface UserDAO {

    public void createUser(UserData userData) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void updateUser(UserData userData) throws DataAccessException;
    public void deleteUser(String username) throws  DataAccessException;
    public void clearAllUsers() throws DataAccessException;
}
