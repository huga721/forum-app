package huberts.spring.forumapp.exception;

public class LikeExistException extends RuntimeException{
    public LikeExistException(String message) {
        super(message);
    }
}