package huberts.spring.forumapp.exception.category;

public class CategoryExistException extends RuntimeException{
    public CategoryExistException(String message) {
        super(message);
    }
}
