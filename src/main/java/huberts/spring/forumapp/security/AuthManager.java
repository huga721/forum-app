package huberts.spring.forumapp.security;

import huberts.spring.forumapp.exception.user.AccountBlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthManager implements AuthenticationManager {

    private final DetailsService service;
    private final PasswordEncoder passwordEncoder;

    private final static String ACCOUNT_BLOCKED_EXCEPTION = "User is blocked.";
    private final static String LOGIN_OR_PASSWORD_IS_WRONG = "Login or password is blank or empty.";
    private final static String PASSWORD_IS_WRONG = "Password is wrong.";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        if (username.isBlank() || password.isBlank()) {
            log.error("An exception occurred!", new BadCredentialsException(LOGIN_OR_PASSWORD_IS_WRONG));
            throw new BadCredentialsException(LOGIN_OR_PASSWORD_IS_WRONG);
        }

        UserDetails user = service.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())){
            log.error("An exception occurred!", new BadCredentialsException(PASSWORD_IS_WRONG));
            throw new BadCredentialsException(PASSWORD_IS_WRONG);
        }
        if (!user.isAccountNonLocked()) {
            log.error("An exception occurred!", new AccountBlockedException(ACCOUNT_BLOCKED_EXCEPTION));
            throw new AccountBlockedException(ACCOUNT_BLOCKED_EXCEPTION);
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}