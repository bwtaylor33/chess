package service;

/**
 * Exception used for proper handling of prohibited requests (403).
 */
public class ForbiddenRequestException extends ResponseException {
    public ForbiddenRequestException(String message) {
        super(message);
    }
}
