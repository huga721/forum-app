package huberts.spring.forumapp.user;

import huberts.spring.forumapp.exception.UserDoesntExistException;
import huberts.spring.forumapp.exception.UsernameOrPasswordIsBlankOrEmpty;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.role.RoleRepository;
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

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceTest {

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
        roleAdmin = new Role(1L, "ROLE_ADMIN", "123");
        roleMod = new Role(2L, "ROLE_MODERATOR", "456");
        roleUser = new Role(3L, "ROLE_USER", "789");

    }

    @DisplayName("Creating user is success, HTTP status 201")
    @Test
    void shouldCreateUserSuccess() {
        // given
        RegisterDTO credentials = new RegisterDTO("user", "password");
        User user = new User(1L, "user", "encoded_password", false, roleUser, null, null);
        UserDTO userDTO = new UserDTO("user", "ROLE_USER", null, false, null);

        // when
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userMapper.buildUser("user", "encoded_password", roleUser)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.buildUserDTO(user)).thenReturn(userDTO);

        UserDTO savedByService = userService.addUser(credentials);

        // then
        assertNotNull(savedByService);
        assertEquals(savedByService.getUsername(), user.getUsername());
        verify(userRepository).save(user);
    }

    @DisplayName("Creating user is failed, username is blank, exception thrown")
    @Test
    void shouldNotCreateUserUsernameIsBlank() {
        // given
        RegisterDTO user = new RegisterDTO("", "password");

        // when
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        // then
        assertThrows(UsernameOrPasswordIsBlankOrEmpty.class, () -> userService.addUser(user));
    }

    @DisplayName("Creating user is failed, password is blank, exception thrown")
    @Test
    void shouldNotCreateUserPasswordIsBlank() {
        // given
        RegisterDTO user = new RegisterDTO("user", "");

        // when
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        // then
        assertThrows(UsernameOrPasswordIsBlankOrEmpty.class, () -> userService.addUser(user));
    }

    @DisplayName("Should return list of all users")
    @Test
    void shouldReturnListOfUsers() {
        // given
        List<User> users = new ArrayList<>();
        User user1 = User.builder().username("user1").password("pass").build();
        User user2 = User.builder().username("user2").password("pass").build();
        users.add(user1);
        users.add(user2);

        List<UserDTO> userDTOS = new ArrayList<>();
        UserDTO us1 = UserDTO.builder().username("user1").build();
        UserDTO us2 = UserDTO.builder().username("user2").build();
        userDTOS.add(us1);
        userDTOS.add(us2);

        given(userRepository.findAll()).willReturn(users);

        // when
        when(userMapper.mapFromList(anyList())).thenReturn(userDTOS);
        List<UserDTO> usersDTO = userService.findAllUsers();

        // then
        assertThat(usersDTO, isNotNull()).hasSize(2);
    }

    @DisplayName("Should return list of every admin and moderator")
    @Test
    void shouldReturnListOfAdminsAndModerators() {
        // given
        List<User> users = new ArrayList<>();
        List<UserDTO> userDTOs = new ArrayList<>();

        User adminUser = User.builder().role(roleAdmin).build();
        users.add(adminUser);

        User moderatorUser = User.builder().role(roleMod).build();
        users.add(moderatorUser);

        User user = User.builder().role(roleUser).build();
        users.add(user);

        UserDTO adminUserDTO = UserDTO.builder().role("ROLE_ADMIN").build();
        userDTOs.add(adminUserDTO);

        UserDTO moderatorUserDTO = UserDTO.builder().role("ROLE_MODERATOR").build();
        userDTOs.add(moderatorUserDTO);

        // when
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.mapFromList(users)).thenReturn(userDTOs);

        List<UserDTO> result = userService.findAllStaffUsers();

        // then
        assertEquals(2, result.size());
        assertEquals("ROLE_ADMIN", result.get(0).getRole());
        assertEquals("ROLE_MODERATOR", result.get(1).getRole());
    }

    @DisplayName("Should return user")
    @Test
    void returnUserSuccess() {
        // given
        User expected = new User();

        // when
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.findByUsername(anyString())).thenReturn(expected);
        User user = userService.findUser("username");

        // then
        assertNotNull(user);
    }


    @DisplayName("Should not return user, user doesnt exist in database, exception thrown")
    @Test
    void doesntReturnUser() {
        // when & then
        when(userRepository.existsByUsername("username")).thenReturn(false);
        assertThrows(UserDoesntExistException.class, () -> userService.findUser("username"));
    }


    @DisplayName("Should delete user")
    @Test
    void deleteUserIsSuccess() {
        // given
        User user = User.builder().username("username").build();

        // when
        when(userRepository.existsByUsername("username")).thenReturn(true);
        when(userRepository.findByUsername("username")).thenReturn(user);

        // then
        userService.deleteUserByUsername("username");
        verify(userRepository, times(1)).delete(user);
    }

    @DisplayName("Return current user")
    @Test
    void currentLoggedUser() {
        // given
        User user = User.builder().username("username").build();
        UserDTO userDTO = UserDTO.builder().username("username").build();

        // when
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(userMapper.buildUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.currentUser("username");

        // then
        assertNotNull(result);
    }

    @DisplayName("Should change role of given user")
    @Test
    void changeRole() {
        // given
        User user = User.builder().username("username").role(roleUser).build();
        UserDTO expected = UserDTO.builder().username("username").role("ROLE_ADMIN").build();

        // when
        when(userRepository.existsByUsername("username")).thenReturn(true);
        when(roleRepository.existsByName("ROLE_ADMIN")).thenReturn(true);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(roleAdmin);
        when(userMapper.buildUserDTO(user)).thenReturn(expected);

        // then
        UserDTO userDTO = userService.changeRole("username", "ROLE_ADMIN");
        User expectedRole = userRepository.findByUsername("username");

        assertNotNull(userDTO);
        assertEquals(userDTO.getRole(), expected.getRole());
        assertEquals(expectedRole.getRole().getName(), userDTO.getRole());
    }

    @DisplayName("Ban user by username")
    @Test
    void shouldBanUser() {
        // given
        User user = User.builder().username("username").blocked(false).build();
        UserDTO userDTO = UserDTO.builder().username("username").blocked(true).build();

        // when
        when(userRepository.existsByUsername("username")).thenReturn(true);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(userMapper.buildUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.banUser("username");

        // then
        assertNotNull(result);
        assertEquals(userDTO, result);
        assertTrue(result.isBlocked());
    }

    @DisplayName("Unban user by username")
    @Test
    void shouldUnbanUser() {
        // given
        User user = User.builder().username("username").blocked(true).build();
        UserDTO userDTO = UserDTO.builder().username("username").blocked(false).build();

        // when
        when(userRepository.existsByUsername("username")).thenReturn(true);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(userMapper.buildUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.unbanUser("username");

        // then
        assertNotNull(result);
        assertFalse(result.isBlocked());
        assertEquals(userDTO, result);
    }

    @DisplayName("Return UserDTO")
    @Test
    void shouldFindUserDTO() {
        // given
        User user = User.builder().username("username").build();
        UserDTO userDTO = UserDTO.builder().username("username").build();

        // when
        when(userRepository.existsByUsername("username")).thenReturn(true);
        when(userRepository.findByUsername("username")).thenReturn(user);
        when(userMapper.buildUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.findUserDTO("username");

        // then
        assertNotNull(result);
        assertEquals(userDTO, result);
    }
}