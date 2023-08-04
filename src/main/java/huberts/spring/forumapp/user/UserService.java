package huberts.spring.forumapp.user;

import huberts.spring.forumapp.exception.role.RoleDoesntExistException;
import huberts.spring.forumapp.exception.role.RoleException;
import huberts.spring.forumapp.exception.user.UserAdminDeleteException;
import huberts.spring.forumapp.exception.user.UserAlreadyExistException;
import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.role.ERole;
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

    private final static String USER_DOESNT_EXIST_ID_EXCEPTION = "User with id \"%s\" doesn't exists.";
    private final static String USER_DOESNT_EXIST_USERNAME_EXCEPTION = "User with username \"%s\" doesn't exists.";
    private final static String USER_EXIST_EXCEPTION = "User with username \"%s\" already exists.";
    private final static String ROLE_DOESNT_EXIST_EXCEPTION = "Role with name \"%s\" doesn't exist. \n" +
            "Available roles are \"user\", \"moderator\", \"admin\".";
    private final static String USER_HAS_THAT_ROLE_EXCEPTION = "User already has that role.";
    private final static String USER_IS_BANNED_EXCEPTION = "User with username \"%s\" is already banned";
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
            log.error(EXCEPTION_OCCURRED, new UserAlreadyExistException(errorMessage));
            throw new UserAlreadyExistException(errorMessage);
        }

        Role roleUser = findRoleByName(ERole.ROLE_USER);
        log.info("User created");
        return buildAndSaveUser(credentials, roleUser);
    }

    private UserDTO buildAndSaveUser(CredentialsDTO credentials, Role role) {
        String password = passwordEncoder.encode(credentials.password());
        User userBuilt = UserMapper.buildUser(credentials.username(), password, role);
        userRepository.save(userBuilt);
        return UserMapper.buildUserDTO(userBuilt);
    }

    private Role findRoleByName(ERole role) {
        log.info("Finding role with name {}", role);
        return roleRepository.findByName(role)
                .orElseThrow(() -> {
                    String errorMessage = String.format(ROLE_DOESNT_EXIST_EXCEPTION, role);
                    log.error(EXCEPTION_OCCURRED, new RoleDoesntExistException(errorMessage));
                    return new RoleDoesntExistException(errorMessage);
                });
    }

    @Override
    public UserDTO getUserById(Long userId) {
        log.info("Getting user with id {}", userId);
        User user = findUserById(userId);
        return UserMapper.buildUserDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        log.info("Getting user with username {}", username);
        User user = findUserByUsername(username);
        return UserMapper.buildUserDTO(user);
    }

    public User findUserById(Long userId) {
        log.info("Finding user with id {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(USER_DOESNT_EXIST_ID_EXCEPTION, userId);
                    log.error(EXCEPTION_OCCURRED, new UserDoesntExistException(errorMessage));
                    return new UserDoesntExistException(errorMessage);
                });
    }

    public User findUserByUsername(String username) {
        log.info("Finding user with username {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format(USER_DOESNT_EXIST_USERNAME_EXCEPTION, username);
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
    public UserDTO changeRoleById(Long userId, String roleName) {
        User user = findUserById(userId);
        log.info("Changing role of user with id {} to role {}", userId, roleName);
        String currentUsersRole = user.getRole().getName().toString();

        if (currentUsersRole.equals(roleName)) {
            log.error(EXCEPTION_OCCURRED, new RoleException(USER_HAS_THAT_ROLE_EXCEPTION));
            throw new RoleException(USER_HAS_THAT_ROLE_EXCEPTION);
        }

        switch (roleName) {
            case "user" -> {
                Role roleUser = findRoleByName(ERole.ROLE_USER);
                user.setRole(roleUser);
            }
            case "moderator" -> {
                Role roleModerator = findRoleByName(ERole.ROLE_MODERATOR);
                user.setRole(roleModerator);
            }
            case "admin" -> {
                Role roleAdmin = findRoleByName(ERole.ROLE_ADMIN);
                user.setRole(roleAdmin);
            }
            default -> {
                String errorMessage = String.format(ROLE_DOESNT_EXIST_EXCEPTION, roleName);
                log.error(EXCEPTION_OCCURRED, new RoleDoesntExistException(errorMessage));
                throw new RoleDoesntExistException(errorMessage);
            }
        }
        log.info("Role changed");
        return UserMapper.buildUserDTO(user);
    }

    @Override
    public UserDTO changePasswordById(PasswordDTO passwordDTO, Long userId) {
        log.info("Changing password for user with id {}", userId);
        String encodedPassword = passwordEncoder.encode(passwordDTO.password());

        User user = findUserById(userId);

        user.setPassword(encodedPassword);
        log.info("Password changed");
        return UserMapper.buildUserDTO(user);
    }

    @Override
    public UserDTO changePasswordByUsername(PasswordDTO passwordDTO, String username) {
        log.info("Changing password for user with username {}", username);
        String encodedPassword = passwordEncoder.encode(passwordDTO.password());

        User user = findUserByUsername(username);

        user.setPassword(encodedPassword);
        log.info("Password changed");
        return UserMapper.buildUserDTO(user);
    }

    @Override
    public UserDTO banUserById(Long userId) {
        log.info("Banning user with id {}", userId);
        User userFound = findUserById(userId);
        log.info(String.valueOf(userFound.isBlocked()));
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
    public UserDTO unbanUserById(Long userId) {
        log.info("Unbanning user with username {}", userId);
        User userFound = findUserById(userId);

        warningRepository.deleteWarningsByUser(userFound);

        userFound.setBlocked(false);
        log.info("User unbanned");
        return UserMapper.buildUserDTO(userFound);
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("Deleting authorized user with id {}", userId);
        User user = findUserById(userId);

        if (isUserAdminOrModerator(user)) {
            log.error(EXCEPTION_OCCURRED, new UserAdminDeleteException(ADMIN_OR_MODERATOR_DELETE_EXCEPTION));
            throw new UserAdminDeleteException(ADMIN_OR_MODERATOR_DELETE_EXCEPTION);
        }

        userRepository.delete(user);
        log.info("Deleted user");
    }

    @Override
    public void deleteUserByUsername(String username) {
        log.info("Deleting authorized user with username {}", username);
        User user = findUserByUsername(username);

        if (isUserAdminOrModerator(user)) {
            log.error(EXCEPTION_OCCURRED, new UserAdminDeleteException(ADMIN_OR_MODERATOR_DELETE_EXCEPTION));
            throw new UserAdminDeleteException(ADMIN_OR_MODERATOR_DELETE_EXCEPTION);
        }

        userRepository.delete(user);
        log.info("Deleted user");
    }

    private boolean isUserAdminOrModerator(User user) {
        Role role = user.getRole();
        return role.getName().toString().equals("ROLE_ADMIN") ||
                role.getName().toString().equals("ROLE_MODERATOR");
    }
}