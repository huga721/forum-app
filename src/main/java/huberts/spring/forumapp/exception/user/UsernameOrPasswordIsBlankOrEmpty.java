package huberts.spring.forumapp.exception.user;

public class UsernameOrPasswordIsBlankOrEmpty extends RuntimeException{
    public UsernameOrPasswordIsBlankOrEmpty(String message) {
        super(message);
    }
}
