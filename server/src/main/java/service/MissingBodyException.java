package service;

public class MissingBodyException extends ResponseException {
    public MissingBodyException() {
        super("Error: missing body");
    }
}
