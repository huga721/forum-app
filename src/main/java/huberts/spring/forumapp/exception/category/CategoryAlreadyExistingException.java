package huberts.spring.forumapp.exception.category;

public class CategoryAlreadyExistingException extends RuntimeException{
    public CategoryAlreadyExistingException(String message) {
        super(message);
    }
}
