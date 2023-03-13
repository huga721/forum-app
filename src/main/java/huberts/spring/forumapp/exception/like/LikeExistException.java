package huberts.spring.forumapp.exception.like;

public class LikeExistException extends RuntimeException{
    public LikeExistException(String message) {
        super(message);
    }
}