package huberts.spring.forumapp.exception;

public class UsernameOrPasswordIsBlankOrEmpty extends RuntimeException{
    public UsernameOrPasswordIsBlankOrEmpty(String message) {
        super(message);
    }
}
