package huberts.spring.forumapp.exception;

public class WarningExistException extends RuntimeException {
    public WarningExistException(String message) {
        super(message);
    }
}