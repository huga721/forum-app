package huberts.spring.forumapp.exception.warning;

public class WarningDoesntExistException extends RuntimeException {
    public WarningDoesntExistException(String message) {
        super(message);
    }
}