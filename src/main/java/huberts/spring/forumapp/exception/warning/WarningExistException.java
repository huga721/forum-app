package huberts.spring.forumapp.exception.warning;

public class WarningExistException extends RuntimeException {
    public WarningExistException(String message) {
        super(message);
    }
}