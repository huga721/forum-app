package huberts.spring.forumapp.exception.user;

public class UserAlreadyExistingException extends RuntimeException {
    public UserAlreadyExistingException(String message) {
        super(message);
    }
}
