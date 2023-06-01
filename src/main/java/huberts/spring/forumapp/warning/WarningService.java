package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.exception.warning.WarningDoesntExistException;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.warning.dto.WarningDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarningService implements WarningServiceApi {

    private static final String USER_DOESNT_EXIST_EXCEPTION = "User \"%s\" doesn't exist.";
    private static final String USER_BANNED_EXCEPTION = "User \"%s\" is banned.";
    private static final String USER_IS_NOT_WARNED_EXCEPTION = "User \"%s\" is not warned.";
    private static final String WARNING_DOESNT_EXIST_EXCEPTION = "Warning with id \"%d\" doesn't exist.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    private final WarningRepository warningRepository;
    private final UserRepository userRepository;

    @Override
    public WarningDTO createWarning(String username) {
        log.info("Creating a warning for a user with username {}", username);
        User user = findUserByUsername(username);
        validateUserBlocked(user);
        log.info("Warning created");
        return buildAndSaveWarning(user);
    }

    public WarningDTO buildAndSaveWarning(User user) {
        Warning warning = WarningMapper.buildWarning(user);
        log.info("BEFORE SAVE = {}", user.getWarnings().size());
        warningRepository.save(warning);
        user.getWarnings().add(warning);
        log.info("AFTER SAVE = {}", user.getWarnings().size());
        if (user.getWarnings().size() >= 5) {
            log.debug("Blocking user {} because number of his/her warnings is 5", user.getUsername());
            user.setBlocked(true);
        }
        return WarningMapper.buildWarningDTO(warning);
    }

    private void validateUserBlocked(User user) {
        if (user.isBlocked()) {
            String errorMessage = String.format(USER_BANNED_EXCEPTION, user.getUsername());
            log.error(EXCEPTION_OCCURRED, new UserBlockException(errorMessage));
            throw new UserBlockException(errorMessage);
        }
    }

    private User findUserByUsername(String username) {
        log.info("Fining user with username {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format(USER_DOESNT_EXIST_EXCEPTION, username);
                    log.error(EXCEPTION_OCCURRED, new UserDoesntExistException(errorMessage));
                    return new UserDoesntExistException(errorMessage);
                });
    }

    @Override
    public WarningDTO getWarningById(Long warningId) {
        log.info("Getting warning by id {}", warningId);
        return WarningMapper.buildWarningDTO(warningRepository.findById(warningId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(WARNING_DOESNT_EXIST_EXCEPTION, warningId);
                    log.error(EXCEPTION_OCCURRED, new WarningDoesntExistException(errorMessage));
                    return new WarningDoesntExistException(errorMessage);
                }));
    }

    @Override
    public List<WarningDTO> getAllWarnings() {
        log.info("Getting all warnings");
        return WarningMapper.mapWarningListToWarningDTOList(warningRepository.findAll());
    }

    @Override
    public void deleteWarning(String username) {
        log.info("Deleting last warning of user {}", username);
        User user = findUserByUsername(username);
        validateUserBlocked(user);
        if (user.getWarnings().isEmpty()) {
            String errorMessage = String.format(USER_IS_NOT_WARNED_EXCEPTION, username);
            log.error(EXCEPTION_OCCURRED, new UserBlockException(errorMessage));
            throw new UserBlockException(errorMessage);
        }
        int lastWarning = user.getWarnings().size() - 1;
        Warning warning = user.getWarnings().get(lastWarning);
        user.getWarnings().remove(warning);
        log.info("Deleted last warning of user {}", username);
        warningRepository.delete(warning);
    }
}