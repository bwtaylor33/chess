package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class CriticalDatabaseException extends RuntimeException{

    public CriticalDatabaseException(String message) {
        super(message);
    }
    public CriticalDatabaseException(String message, Throwable ex) {
        super(message, ex);
    }
}
