package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.exception.warning.WarningExistException;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.user.service.UserService;
import huberts.spring.forumapp.warning.dto.WarningDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WarningService implements WarningServiceApi {

    private final WarningRepository warningRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    private static final String USER_DOESNT_EXIST_EXCEPTION = "User \"%s\" doesn't exist.";
    private static final String USER_BANNED_EXCEPTION = "User \"%s\" is banned.";
    private static final String USER_IS_NOT_WARNED_EXCEPTION = "User \"%s\" is not warned.";
    private static final String WARNING_DOESNT_EXIST_EXCEPTION = "Warning with id \"%d\" doesn't exist.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    @Override
    public WarningDTO giveWarning(String username) {
        User userWarned = findByUsername(username);
        log.info("Creating a warning for a user with username {}", username);
        if (userWarned.isBlocked()) {
            String errorMessage = String.format(USER_BANNED_EXCEPTION, username);
            log.error(EXCEPTION_OCCURRED, new UserBlockException(errorMessage));
            throw new UserBlockException(errorMessage);
        }
        Warning warningBuilt = WarningMapper.buildWarning(userWarned);
        Warning warningSaved = warningRepository.save(warningBuilt);
        if (userWarned.getWarnings().size() == 5) {
            log.debug("Blocking user {} because number of his/her warnings is 5", username);
            userService.banUser(username);
        }
        log.info("Warning created");
        return WarningMapper.buildWarningDTO(warningSaved);
    }

    private User findByUsername(String username) {
        log.info("Fining user with username {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format(USER_DOESNT_EXIST_EXCEPTION, username);
                    log.error(EXCEPTION_OCCURRED, new UserDoesntExistException(errorMessage));
                    return new UserDoesntExistException(errorMessage);
                });
    }

    @Override
    public WarningDTO getWarningById(Long id) {
        log.info("Getting warning by id {}", id);
        return WarningMapper.buildWarningDTO(warningRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(WARNING_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new WarningExistException(errorMessage));
                    return new WarningExistException(errorMessage);
                }));
    }

    @Override
    public List<WarningDTO> getAllWarnings() {
        log.info("Getting all warnings");
        return WarningMapper.mapWarningsToWarningsDTO(warningRepository.findAll());
    }

    @Override
    public void deleteWarning(String username) {
        User userWarned = findByUsername(username);
        log.info("Deleting last warning of user {}", username);
        if (userWarned.isBlocked()) {
            String errorMessage = String.format(USER_BANNED_EXCEPTION, username);
            log.error(EXCEPTION_OCCURRED, new UserBlockException(errorMessage));
            throw new UserBlockException(errorMessage);
        }
        if (userWarned.getWarnings().isEmpty()) {
            String errorMessage = String.format(USER_IS_NOT_WARNED_EXCEPTION, username);
            log.error(EXCEPTION_OCCURRED, new UserBlockException(errorMessage));
            throw new UserBlockException(errorMessage);
        }
        int lastWarning = userWarned.getWarnings().size() - 1;
        Warning warning = userWarned.getWarnings().get(lastWarning);
        log.info("Deleted last warning of user {}", username);
        warningRepository.delete(warning);
    }
}