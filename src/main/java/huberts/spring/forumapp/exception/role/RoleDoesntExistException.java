package huberts.spring.forumapp.exception.role;

public class RoleDoesntExistException extends RuntimeException{
    public RoleDoesntExistException(String message) {
        super(message);
    }
}
