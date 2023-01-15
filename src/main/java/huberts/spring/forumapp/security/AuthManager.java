package huberts.spring.forumapp.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthManager implements AuthenticationManager {
    private final DetailsService service;

    /**
     * Method authenticate user, service is looking for the user in db, if the user exist then creates new token and return it
     * If it doesn't exist it should throw an exception
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Retrieving a UserDetails object from method which find the user by username in db
        UserDetails user = service.loadUserByUsername(authentication.getPrincipal().toString());

        log.info("AuthManager call \'authenticate\' method");
        log.info("username - {} | password - {}", user.getUsername(), user.getPassword());
        // Returning new UsernamePasswordAuthenticationToken
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
