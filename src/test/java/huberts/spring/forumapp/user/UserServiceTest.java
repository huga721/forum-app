package huberts.spring.forumapp.user;

import huberts.spring.forumapp.exception.RoleException;
import huberts.spring.forumapp.exception.UserBlockException;
import huberts.spring.forumapp.exception.UserDoesntExistException;
import huberts.spring.forumapp.exception.UsernameOrPasswordIsBlankOrEmpty;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.role.RoleRepository;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceTest {

    private static final String USERNAME_USER = "user";
    private static final String USERNAME_TEST = "test";
    private static final String USERNAME_BLANK = "";
    private static final String ROLE_NAME_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_NAME_MODERATOR = "ROLE_MODERATOR";
    private static final String ROLE_NAME_USER = "ROLE_USER";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_TO_CHANGE = "passwordTest";
    private static final String PASSWORD_ENCODED = "encoded_password";
    private static final String PASSWORD_EMPTY = "";

    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserService userService;

    private Role roleAdmin;
    private Role roleMod;
    private Role roleUser;

    @BeforeEach
    void setUp() {
        roleAdmin = Role.builder()
                .name(ROLE_NAME_ADMIN)
                .build();
        roleMod = Role.builder()
                .name(ROLE_NAME_MODERATOR)
                .build();
        roleUser = Role.builder()
                .name(ROLE_NAME_USER)
                .build();

    }

    @DisplayName("should create and save user")
    @Test
    void shouldCreateUserSuccess() {
        // given
        RegisterDTO credentials = new RegisterDTO(USERNAME_USER, PASSWORD);
        User user = User.builder()
                .username(USERNAME_USER)
                .password(PASSWORD_ENCODED)
                .role(roleUser)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .role(ROLE_NAME_USER)
                .build();
        // when
        when(passwordEncoder.encode(any(String.class))).thenReturn(PASSWORD_ENCODED);
        when(roleRepository.findByName(any(String.class))).thenReturn(roleUser);
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(userMapper.buildUser(any(String.class), any(String.class), any(Role.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO savedByService = userService.addUser(credentials);
        // then
        assertNotNull(savedByService);
        assertEquals(savedByService.getUsername(), user.getUsername());
        verify(userRepository).save(user);
    }

    @DisplayName("should not create user when username is blank")
    @Test
    void shouldNotCreateUserUsernameIsBlank() {
        // given
        RegisterDTO user = new RegisterDTO(USERNAME_BLANK, PASSWORD);
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        // then
        assertThrows(UsernameOrPasswordIsBlankOrEmpty.class, () -> userService.addUser(user));
    }

    @DisplayName("should not create user when password is blank")
    @Test
    void shouldNotCreateUserPasswordIsBlank() {
        // given
        RegisterDTO user = new RegisterDTO(USERNAME_USER, PASSWORD_EMPTY);
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        // then
        assertThrows(UsernameOrPasswordIsBlankOrEmpty.class, () -> userService.addUser(user));
    }

    @DisplayName("should return list of all users")
    @Test
    void shouldReturnListOfUsers() {
        // given
        List<User> users = new ArrayList<>();
        List<UserDTO> usersDTO = new ArrayList<>();

        User userOne = User.builder()
                .username(USERNAME_USER)
                .password(PASSWORD)
                .build();
        User userTwo = User.builder()
                .username(USERNAME_USER)
                .password(PASSWORD)
                .build();

        UserDTO userOneDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .build();
        UserDTO userTwoDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .build();

        users.add(userOne);
        users.add(userTwo);
        usersDTO.add(userOneDTO);
        usersDTO.add(userTwoDTO);

        given(userRepository.findAll()).willReturn(users);
        // when
        when(userMapper.mapFromList(anyList())).thenReturn(usersDTO);
        List<UserDTO> result = userService.findAllUsers();
        // then
        assertNotNull(result);
        assertEquals(usersDTO.size(), result.size());
    }

    @DisplayName("should return list of every admin and moderator")
    @Test
    void shouldReturnListOfAdminsAndModerators() {
        // given
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();

        User adminUser = User.builder().role(roleAdmin).build();
        User moderatorUser = User.builder().role(roleMod).build();
        User user = User.builder().role(roleUser).build();

        users.add(adminUser);
        users.add(moderatorUser);
        users.add(user);

        UserDTO adminUserDTO = UserDTO.builder()
                .role(ROLE_NAME_ADMIN)
                .build();
        userDTOs.add(adminUserDTO);

        UserDTO moderatorUserDTO = UserDTO.builder()
                .role(ROLE_NAME_MODERATOR)
                .build();
        userDTOs.add(moderatorUserDTO);
        // when
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.mapFromList(anyList())).thenReturn(userDTOs);

        List<UserDTO> result = userService.findAllStaffUsers();
        // then
        assertEquals(2, result.size());
        assertEquals(ROLE_NAME_ADMIN, result.get(0).getRole());
        assertEquals(ROLE_NAME_MODERATOR, result.get(1).getRole());
    }

    @DisplayName("should return user")
    @Test
    void returnUserSuccess() {
        // given
        User user = User.builder()
                .id(1L)
                .username(USERNAME_TEST)
                .role(roleUser)
                .build();
        // when
        when(userRepository.existsByUsername(eq(USERNAME_TEST))).thenReturn(true);
        when(userRepository.findByUsername(eq(USERNAME_TEST))).thenReturn(user);

        User result = userService.findUser(USERNAME_TEST);
        // then
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
    }


    @DisplayName("should not return user when user doesnt exist")
    @Test
    void shouldNotReturnUser() {
        // when & then
        when(userRepository.existsByUsername(USERNAME_USER)).thenReturn(false);
        assertThrows(UserDoesntExistException.class, () -> userService.findUser(USERNAME_USER));
    }


    @DisplayName("should delete user")
    @Test
    void shouldDeleteUser() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        // then
        userService.deleteUserByUsername(USERNAME_USER);
        verify(userRepository, times(1)).delete(user);
    }

    @DisplayName("should return current user")
    @Test
    void shouldReturnCurrentLoggedUser() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.currentUser(USERNAME_USER);
        // then
        assertNotNull(result);
    }

    @DisplayName("should change password of given user")
    @Test
    void shouldChangePassword() {
        // given
        PasswordDTO password = new PasswordDTO(PASSWORD_TO_CHANGE);

        User user = User.builder()
                .username(USERNAME_USER)
                .password(PASSWORD)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .build();
        // when
        when(passwordEncoder.encode(any(String.class))).thenReturn(PASSWORD_TO_CHANGE);
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.changePassword(password, USERNAME_USER);
        // then
        assertNotNull(result);
    }

    @DisplayName("should not change password when password is blank")
    @Test
    void shouldNotChangePassword_PasswordIsBlank() {
        // given
        PasswordDTO password = new PasswordDTO(PASSWORD_EMPTY);
        // when & then
        assertThrows(UsernameOrPasswordIsBlankOrEmpty.class, () -> userService.changePassword(password,USERNAME_USER));
    }

    @DisplayName("should change role of given user")
    @Test
    void shouldChangeRole() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .role(roleUser)
                .build();
        UserDTO expected = UserDTO.builder()
                .username(USERNAME_USER)
                .role(ROLE_NAME_ADMIN)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(roleRepository.existsByName(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(roleRepository.findByName(any(String.class))).thenReturn(roleAdmin);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(expected);
        // then
        UserDTO userDTO = userService.changeRole(USERNAME_USER, ROLE_NAME_ADMIN);
        User expectedRole = userRepository.findByUsername(USERNAME_USER);

        assertNotNull(userDTO);
        assertEquals(userDTO.getRole(), expected.getRole());
        assertEquals(expectedRole.getRole().getName(), userDTO.getRole());
    }

    @DisplayName("should not change role when user already has that role")
    @Test
    void shouldNotChangeRole() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .role(roleMod)
                .build();
        UserDTO expected = UserDTO.builder()
                .username(USERNAME_USER)
                .role(ROLE_NAME_ADMIN)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(roleRepository.existsByName(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(roleRepository.findByName(any(String.class))).thenReturn(roleMod);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(expected);
        // then
        assertThrows(RoleException.class, () -> userService.changeRole(USERNAME_USER, ROLE_NAME_MODERATOR));
    }

    @DisplayName("should ban user by given username")
    @Test
    void shouldBanUser() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .blocked(false)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .blocked(true)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.banUser(USERNAME_USER);
        // then
        assertNotNull(result);
        assertEquals(userDTO, result);
        assertTrue(result.isBlocked());
    }

    @DisplayName("should not ban user when user is already blocked")
    @Test
    void shouldNotBanUser_AlreadyBanned() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .blocked(true)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        // then
        assertThrows(UserBlockException.class, () -> userService.banUser(USERNAME_USER));
    }

    @DisplayName("should unban user by given username")
    @Test
    void shouldUnbanUser() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .blocked(true)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .blocked(false)
                .build();

        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.unbanUser(USERNAME_USER);

        // then
        assertNotNull(result);
        assertFalse(result.isBlocked());
        assertEquals(userDTO, result);
    }

    @DisplayName("should not unban user when user is not banned")
    @Test
    void shouldNotUnbanUser() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .blocked(false)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        // then
        assertThrows(UserBlockException.class, () -> userService.unbanUser(USERNAME_USER));
    }

    @DisplayName("should return userDTO")
    @Test
    void shouldFindUserDTO() {
        // given
        User user = User.builder()
                .username(USERNAME_USER)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .username(USERNAME_USER)
                .build();
        // when
        when(userRepository.existsByUsername(any(String.class))).thenReturn(true);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userMapper.buildUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.findUserDTO(USERNAME_USER);
        // then
        assertNotNull(result);
        assertEquals(userDTO, result);
    }
}