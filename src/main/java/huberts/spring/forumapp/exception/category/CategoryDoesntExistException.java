package huberts.spring.forumapp.exception.category;

public class CategoryDoesntExistException extends RuntimeException{
    public CategoryDoesntExistException(String message) {
        super(message);
    }
}