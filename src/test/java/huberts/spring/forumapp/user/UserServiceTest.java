package huberts.spring.forumapp.user;

import huberts.spring.forumapp.exception.role.RoleDoesntExistException;
import huberts.spring.forumapp.exception.role.RoleException;
import huberts.spring.forumapp.exception.user.UserAlreadyExistingException;
import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.role.RoleRepository;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import huberts.spring.forumapp.user.service.UserService;
import huberts.spring.forumapp.warning.WarningRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final String ROLE_NAME_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_NAME_MODERATOR = "ROLE_MODERATOR";
    private static final String ROLE_NAME_USER = "ROLE_USER";
    public static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_TO_CHANGE = "passwordTest";
    private static final String PASSWORD_ENCODED = "encoded_password";
    public static final String EMPTY = "";

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WarningRepository warningRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserService userService;

    private static Role ROLE_ADMIN;
    private static Role ROLE_MODERATOR;
    private static Role ROLE_USER;
    private User user;
    private User moderator;
    private User admin;

    @BeforeAll
    static void beforeAll() {
        ROLE_ADMIN = Role.builder()
                .name(ROLE_NAME_ADMIN)
                .build();
        ROLE_MODERATOR = Role.builder()
                .name(ROLE_NAME_MODERATOR)
                .build();
        ROLE_USER = Role.builder()
                .name(ROLE_NAME_USER)
                .build();
    }

    @BeforeEach
    void setUp() {
        user = createUser(ROLE_USER);
        moderator = createUser(ROLE_MODERATOR);
        admin = createUser(ROLE_ADMIN);
    }

    private User createUser(Role role) {
        return User.builder()
                .username(USER)
                .password(UserServiceTest.PASSWORD)
                .role(role)
                .topics(List.of())
                .warnings(List.of())
                .build();
    }

    @DisplayName("createUser method")
    @Nested
    class CreateUserTests {

        private RegisterDTO credentials;

        @BeforeEach
        void setUp() {
            credentials = new RegisterDTO(USER, PASSWORD);
        }

        @DisplayName("Should create and save a user")
        @Test
        void shouldCreateUser() {
            when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
            when(passwordEncoder.encode(any(String.class))).thenReturn(PASSWORD_ENCODED);
            when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(ROLE_USER));
            when(userRepository.save(any(User.class))).thenReturn(user);
            UserDTO userSaved = userService.createUser(credentials);

            assertNotNull(userSaved);
            verify(userRepository, times(1)).save(any(User.class));
        }

        @DisplayName("Should throw UserAlreadyExistingException when user exists")
        @Test
        void shouldThrowUserAlreadyExistingException_WhenUserExists() {
            when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
            assertThrows(UserAlreadyExistingException.class, () -> userService.createUser(credentials));
        }

        @DisplayName("Should throw RoleDoesntExistException when role doesn't exist")
        @Test
        void shouldThrowRoleDoesntExistException_WhenRoleDoesntExist() {
            when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
            assertThrows(RoleDoesntExistException.class, () -> userService.createUser(credentials));
        }
    }

    @DisplayName("getUserByUsername method")
    @Nested
    class GetUserByUsernameTests {

        @DisplayName("Should return user by username")
        @Test
        void shouldReturnUser() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            UserDTO expected = userService.getUserByUsername(USER);

            assertNotNull(expected);
            verify(userRepository, times(1)).findByUsername(USER);
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> userService.getUserByUsername(USER));
        }
    }

    @DisplayName("getAllUsers method")
    @Nested
    class GetAllUsersTests {

        @DisplayName("Should return list of all users")
        @Test
        void shouldReturnListOfUsers() {
            List<User> users = List.of(user, moderator, admin);

            given(userRepository.findAll()).willReturn(users);
            List<UserDTO> actual = userService.getAllUsers();

            assertNotNull(actual);
            assertEquals(users.size(), actual.size());
            verify(userRepository, times(1)).findAll();
        }
    }

    @DisplayName("getAllModeratorAndAdminUsers method")
    @Nested
    class GetAllModeratorAndAdminUsersTests {

        @DisplayName("Should return list of every admin and moderator users")
        @Test
        void shouldReturnListOfAdminsAndModerators() {
            List<User> users = List.of(user, admin, moderator);

            when(userRepository.findAll()).thenReturn(users);
            List<UserDTO> result = userService.getAllModeratorAndAdminUsers();

            assertEquals(users.size() - 1, result.size());
            assertEquals(ROLE_NAME_ADMIN, result.get(0).getRole());
            assertEquals(ROLE_NAME_MODERATOR, result.get(1).getRole());
            verify(userRepository, times(1)).findAll();
        }
    }

    @DisplayName("changeRole method")
    @Nested
    class ChangeRoleTests {

        @DisplayName("Should change role")
        @Test
        void shouldChangeRole() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(ROLE_ADMIN));
            UserDTO userDTO = userService.changeRole(USER, ROLE_NAME_ADMIN);

            assertNotNull(userDTO);
            assertEquals(userDTO.getRole(), ROLE_NAME_ADMIN);
            verify(userRepository, times(1)).findByUsername(USER);
        }

        @DisplayName("Should throw RoleException when role to be changed is the same as actual role")
        @Test
        void shouldThrowRoleException_WhenRoleToBeChangedIsTheSameAsActualRole() {
            user.setRole(ROLE_MODERATOR);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(RoleException.class, () -> userService.changeRole(USER, ROLE_NAME_MODERATOR));
        }

        @DisplayName("Should throw RoleDoesntException when role to be changed doesn't exist")
        @Test
        void shouldThrowRoleDoesntExistException_WhenRoleToBeChangedDoesntExist() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(RoleDoesntExistException.class, () -> userService.changeRole(USER, EMPTY));
        }


        @DisplayName("Should throw UserDoesntExistException when user to change role doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserToChangeRoleDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> userService.changeRole(USER, ROLE_NAME_ADMIN));
        }
    }

    @DisplayName("changePassword method")
    @Nested
    class ChangePasswordTests {

        private PasswordDTO password;

        @BeforeEach
        void setUp() {
            password = new PasswordDTO(PASSWORD_TO_CHANGE);
        }

        @DisplayName("Should change password")
        @Test
        void shouldChangePassword() {
            when(passwordEncoder.encode(any(String.class))).thenReturn(PASSWORD_TO_CHANGE);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            UserDTO result = userService.changePassword(password, USER);

            assertNotNull(result);
            verify(userRepository, times(1)).findByUsername(USER);
        }

        @DisplayName("Should throw UserDoesntExist when user to change password doesn't exist")
        @Test
        void shouldThrowUserDoesntExist_WhenUserToChangePasswordDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> userService.changePassword(password, USER));
        }
    }

    @DisplayName("banUser method")
    @Nested
    class BanUserTests {
        @DisplayName("Should ban user")
        @Test
        void shouldBanUser() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            UserDTO result = userService.banUser(USER);

            assertNotNull(result);
            assertTrue(result.isBlocked());
        }

        @DisplayName("Should throw UserBlockException when user is already banned")
        @Test
        void shouldThrowUserBlockException_WhenUserIsAlreadyBanned() {
            user.setBlocked(true);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(UserBlockException.class, () -> userService.banUser(USER));
        }

        @DisplayName("Should throw UserDoesntExistException when user to ban doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserToBanDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> userService.banUser(USER));
        }
    }

    @DisplayName("unbanUser method")
    @Nested
    class UnbanUserTests {

        @DisplayName("Should unban user")
        @Test
        void shouldUnbanUser() {
            user.setBlocked(true);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            doNothing().when(warningRepository).deleteWarningsByUser(any(User.class));
            UserDTO result = userService.unbanUser(USER);

            assertNotNull(result);
            assertFalse(result.isBlocked());
        }

        @DisplayName("Should throw UserBlockException when user is not banned")
        @Test
        void shouldThrowUserBlockException_WhenUserIsNotBanned() {
            user.setBlocked(false);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

            assertThrows(UserBlockException.class, () -> userService.unbanUser(USER));
        }

        @DisplayName("Should throw UserDoesntExistException when user to ban doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserToBanDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> userService.unbanUser(USER));
        }
    }

    @DisplayName("deleteUserByUsername method")
    @Nested
    class DeleteUserByUsernameTests {

        @DisplayName("Should delete user")
        @Test
        void shouldDeleteUser() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

            userService.deleteUserByUsername(USER);
            verify(userRepository, times(1)).delete(user);
        }

        @DisplayName("Should throw UserDoesntExistException when user to delete doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserToDeleteDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> userService.deleteUserByUsername(USER));
        }
    }
}