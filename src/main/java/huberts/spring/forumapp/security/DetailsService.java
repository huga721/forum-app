package huberts.spring.forumapp.security;

import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        return mapToSecuredUser(user);
    }

    private SecuredUser mapToSecuredUser(User user) {
        return new SecuredUser(user);
    }

    private User getUser(String username) {
        log.debug("Authenticating user \"{}\"", username);
        return userService.findUserByUsername(username);
    }
}
