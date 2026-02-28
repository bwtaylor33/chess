package service;

/**
 * Exception used for proper handling of poorly formed requests (400).
 */
public class BadRequestException extends ResponseException {
    public BadRequestException(String message) {
        super(message);
    }
}
