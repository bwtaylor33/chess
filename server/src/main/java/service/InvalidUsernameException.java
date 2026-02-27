package service;

public class InvalidUsernameException extends ResponseException {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
