package huberts.spring.forumapp.exception;

public class CategoryAlreadyExistingException extends RuntimeException{
    public CategoryAlreadyExistingException(String message) {
        super(message);
    }
}
