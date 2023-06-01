package huberts.spring.forumapp.user;

import huberts.spring.forumapp.exception.role.RoleDoesntExistException;
import huberts.spring.forumapp.exception.role.RoleException;
import huberts.spring.forumapp.exception.user.UserAdminDeleteException;
import huberts.spring.forumapp.exception.user.UserAlreadyExistingException;
import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.role.RoleRepository;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.CredentialsDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import huberts.spring.forumapp.warning.WarningRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserServiceApi {

    private final static String ROLE_USER = "ROLE_USER";

    private final static String USER_DOESNT_EXIST_EXCEPTION = "User with username \"%s\" doesn't exists.";
    private final static String USER_EXIST_EXCEPTION = "User with username \"%s\" already exists.";
    private final static String ROLE_DOESNT_EXIST_EXCEPTION = "Role with name \"%s\" doesn't exist.";
    private final static String USER_HAS_THAT_ROLE_EXCEPTION = "User already has that role.";
    private final static String USER_IS_BANNED_EXCEPTION = "User with username \"%s\" is already banned";
    private final static String USER_IS_NOT_BANNED_EXCEPTION = "User with username \"%s\" is not banned";
    private final static String ADMIN_OR_MODERATOR_DELETE_EXCEPTION = "You can't delete admin or moderator account";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WarningRepository warningRepository;

    @Override
    public UserDTO createUser(CredentialsDTO credentials) {
        log.info("Creating user with nickname {}", credentials.username());
        if (userRepository.existsByUsername(credentials.username())) {
            String errorMessage = String.format(USER_EXIST_EXCEPTION, credentials.username());
            log.error(EXCEPTION_OCCURRED, new UserAlreadyExistingException(errorMessage));
            throw new UserAlreadyExistingException(errorMessage);
        }

        Role roleUser = findRoleByName(ROLE_USER);
        log.info("User created");
        return buildAndSaveUser(credentials, roleUser);
    }

    private UserDTO buildAndSaveUser(CredentialsDTO credentials, Role role) {
        User userBuilt = UserMapper.buildUser(credentials.username(), credentials.password(), role);
        userRepository.save(userBuilt);
        return UserMapper.buildUserDTO(userBuilt);
    }

    private Role findRoleByName(String roleName) {
        log.info("Finding role with name {}", roleName);
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    String errorMessage = String.format(ROLE_DOESNT_EXIST_EXCEPTION, roleName);
                    log.error(EXCEPTION_OCCURRED, new RoleDoesntExistException(errorMessage));
                    return new RoleDoesntExistException(errorMessage);
                });
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        log.info("Getting user with username {}", username);
        User user = findUserByUsername(username);
        return UserMapper.buildUserDTO(user);
    }

    public User findUserByUsername(String username) {
        log.info("Finding user with username {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format(USER_DOESNT_EXIST_EXCEPTION, username);
                    log.error(EXCEPTION_OCCURRED, new UserDoesntExistException(errorMessage));
                    return new UserDoesntExistException(errorMessage);
                });
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Getting all users");
        return UserMapper.mapUserListToUserDTOList(userRepository.findAll());
    }

    @Override
    public List<UserDTO> getAllModeratorAndAdminUsers() {
        log.info("Getting all moderators and admins");
        return UserMapper.mapUserListToUserDTOList(userRepository.findAll())
                .stream()
                .filter(user ->
                        user.role().equals("ROLE_ADMIN") || user.role().equals("ROLE_MODERATOR"))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO changeRole(String username, String roleName) {
        User user = findUserByUsername(username);
        log.info("Changing role of user with username {} to role {}", username, roleName);
        String currentUsersRole = user.getRole().getName();
        if (currentUsersRole.equals(roleName)) {
            log.error(EXCEPTION_OCCURRED, new RoleException(USER_HAS_THAT_ROLE_EXCEPTION));
            throw new RoleException(USER_HAS_THAT_ROLE_EXCEPTION);
        }
        Role roleToChange = findRoleByName(roleName);
        user.setRole(roleToChange);
        log.info("Role changed");
        return UserMapper.buildUserDTO(user);
    }

    @Override
    public UserDTO changePassword(PasswordDTO passwordDTO, String username) {
        log.info("Changing password for user with username {}", username);
        String encodedPassword = passwordEncoder.encode(passwordDTO.password());
        User user = findUserByUsername(username);
        user.setPassword(encodedPassword);
        log.info("Password changed");
        return UserMapper.buildUserDTO(user);
    }

    @Override
    public UserDTO banUser(String username) {
        log.info("Banning user with username {}", username);
        User userFound = findUserByUsername(username);
        validateUserBlocked(userFound);
        userFound.setBlocked(true);
        log.info("User banned");
        return UserMapper.buildUserDTO(userFound);
    }

    private void validateUserBlocked(User user) {
        if (user.isBlocked()) {
            String errorMessage = String.format(USER_IS_BANNED_EXCEPTION, user.getUsername());
            log.error(EXCEPTION_OCCURRED, new UserBlockException(errorMessage));
            throw new UserBlockException(errorMessage);
        }
    }

    @Override
    public UserDTO unbanUser(String username) {
        log.info("Unbanning user with username {}", username);
        User userFound = findUserByUsername(username);
        validateUserBlocked(userFound);

        warningRepository.deleteWarningsByUser(userFound);
        userFound.setBlocked(false);
        log.info("User unbanned");
        return UserMapper.buildUserDTO(userFound);
    }

    @Override
    public void deleteUserByUsername(String username) {
        log.info("Deleting authorized user with username {}", username);
        User userFound = findUserByUsername(username);
        if (userIsAdminOrModerator(userFound)) {
            log.error(EXCEPTION_OCCURRED, new UserAdminDeleteException(ADMIN_OR_MODERATOR_DELETE_EXCEPTION));
            throw new UserAdminDeleteException(ADMIN_OR_MODERATOR_DELETE_EXCEPTION);
        }
        userRepository.delete(userFound);
        log.info("Deleted user");
    }

    private boolean userIsAdminOrModerator(User user) {
        Role role = user.getRole();
        return role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_MODERATOR");
    }
}