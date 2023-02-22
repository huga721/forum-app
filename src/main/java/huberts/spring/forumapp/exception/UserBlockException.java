package huberts.spring.forumapp.exception;

public class UserBlockException extends RuntimeException{
    public UserBlockException(String message) {
        super(message);
    }
}