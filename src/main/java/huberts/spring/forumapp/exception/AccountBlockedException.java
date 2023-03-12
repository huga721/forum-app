package huberts.spring.forumapp.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountBlockedException extends AuthenticationException {
    public AccountBlockedException(String message) { super(message);}
}