package huberts.spring.forumapp.utility;

import huberts.spring.forumapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class UtilityService {

    private final UserRepository userRepository;

    public void updateUserLastActivity(String username) {
        userRepository.findByUsername(username).get()
                .setLastActivity(LocalDateTime.now());
    }
}