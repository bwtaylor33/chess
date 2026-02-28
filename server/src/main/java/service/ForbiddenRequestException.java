package service;

public class ForbiddenRequestException extends ResponseException {
    public ForbiddenRequestException(String message) {
        super(message);
    }
}
