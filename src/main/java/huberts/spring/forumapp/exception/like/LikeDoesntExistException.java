package huberts.spring.forumapp.exception.like;

public class LikeDoesntExistException extends RuntimeException{
    public LikeDoesntExistException(String message) {
        super(message);
    }
}