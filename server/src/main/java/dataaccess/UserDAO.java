package dataaccess;

public interface UserDAO {

    public void createUser(User user) throws DataAccessException;
    public User getUser(String username) throws DataAccessException;
}
