package huberts.spring.forumapp.security;

import huberts.spring.forumapp.exception.AccountBlockedException;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = service.loadUserByUsername(authentication.getPrincipal().toString());

        if (!user.isAccountNonLocked()) {
            throw new AccountBlockedException("User " + user.getUsername() + " is blocked.");
        }

        log.info("username - {} | password - {}", user.getUsername(), user.getPassword());
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}