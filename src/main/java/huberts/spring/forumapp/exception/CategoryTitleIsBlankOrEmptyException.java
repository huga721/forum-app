package huberts.spring.forumapp.exception;

public class CategoryTitleIsBlankOrEmptyException extends RuntimeException{
    public CategoryTitleIsBlankOrEmptyException(String message) {
        super(message);
    }
}