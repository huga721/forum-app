package huberts.spring.forumapp.exception;

public class RoleDoesntExistException extends RuntimeException{
    public RoleDoesntExistException(String message) {
        super(message);
    }
}
