package huberts.spring.forumapp.exception;

public class UserAlreadyExistingException extends RuntimeException {
    public UserAlreadyExistingException(String message) {
        super(message);
    }

    public UserAlreadyExistingException(String message, Throwable cause) {
        super(message, cause);
    }
}
