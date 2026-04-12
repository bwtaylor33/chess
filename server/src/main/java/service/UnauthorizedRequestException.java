package service;

/**
 * Exception used for proper handling of prohibited requests (401).
 */
public class UnauthorizedRequestException extends ResponseException {
    public UnauthorizedRequestException(String message) {
        super(message);
    }
}
