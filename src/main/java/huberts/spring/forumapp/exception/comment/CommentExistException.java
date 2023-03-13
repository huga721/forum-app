package huberts.spring.forumapp.exception.comment;

public class CommentExistException extends RuntimeException {
    public CommentExistException(String message) {
        super(message);
    }
}