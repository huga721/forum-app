package huberts.spring.forumapp.exception.user;

public class UserBlockException extends RuntimeException{
    public UserBlockException(String message) {
        super(message);
    }
}