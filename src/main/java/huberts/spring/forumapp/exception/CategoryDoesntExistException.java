package huberts.spring.forumapp.exception;

public class CategoryDoesntExistException extends RuntimeException{
    public CategoryDoesntExistException(String message) {
        super(message);
    }
}
