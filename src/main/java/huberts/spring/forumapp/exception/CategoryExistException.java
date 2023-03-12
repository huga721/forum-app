package huberts.spring.forumapp.exception;

public class CategoryExistException extends RuntimeException{
    public CategoryExistException(String message) {
        super(message);
    }
}
