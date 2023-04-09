package huberts.spring.forumapp.exception.comment;

public class CommentDoesntExistException extends RuntimeException {
    public CommentDoesntExistException(String message) {
        super(message);
    }
}