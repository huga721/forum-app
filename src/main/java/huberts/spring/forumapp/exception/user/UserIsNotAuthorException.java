package huberts.spring.forumapp.exception.user;

public class UserIsNotAuthorException extends RuntimeException {
    public UserIsNotAuthorException(String message) {
        super(message);
    }
}