package huberts.spring.forumapp.exception;

public class CommentExistException extends RuntimeException {
    public CommentExistException(String message) {
        super(message);
    }
}