package huberts.spring.forumapp.security;

import huberts.spring.forumapp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class        DetailsService implements UserDetailsService {
    private final UserService userService;

    /**
     * Method that search for the user in db
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("DetailsService call \'loadUserByUsername\' - method");
        return userService.findUser(username)
                .map(SecuredUser::new)
                .orElseThrow(() -> new BadCredentialsException("User " + username + " not found."));
    }
}
