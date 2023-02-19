package huberts.spring.forumapp.user;

import huberts.spring.forumapp.exception.RoleDoesntExistException;
import huberts.spring.forumapp.exception.UserAlreadyExistingException;
import huberts.spring.forumapp.exception.UserDoesntExistException;
import huberts.spring.forumapp.exception.UsernameOrPasswordIsBlankOrEmpty;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.role.RoleRepository;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService implements UserServiceApi {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper mapper;

    @Override
    public UserDTO addUser(RegisterDTO user) {
        String username = user.getUsername();
        String password = passwordEncoder.encode(user.getPassword());
        Role roleUser = roleRepository.findByName("ROLE_USER");

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistingException("User " + username + " already existing in database.");
        }
        if (username.isBlank() || user.getPassword().isBlank()) {
            throw new UsernameOrPasswordIsBlankOrEmpty("Username or password is blank or empty.");
        }

        User userCreated = mapper.buildUser(username, password, roleUser);
        userRepository.save(userCreated);

        return mapper.buildUserDTO(userCreated);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return mapper.mapFromList(userRepository.findAll());
    }

    @Override
    public List<UserDTO> findAllStaffUsers() {
        return mapper.mapFromList(userRepository.findAll())
                .stream()
                .filter(user ->
                        user.getRole().equals("ROLE_ADMIN") ||
                                user.getRole().equals("ROLE_MODERATOR"))
                .collect(Collectors.toList());
    }

    @Override
    public User findUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserDoesntExistException("User " + username + " doesn't exist.");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user = findUser(username);
        userRepository.delete(user);
    }

    @Override
    public UserDTO currentUser(String username) {
        User user = userRepository.findByUsername(username);
        return mapper.buildUserDTO(user);
    }

    @Override
    public User currentLoggedUser(String token) {
        return null;
    }

    @Override
    public UserDTO changeRole(String username, String roleName) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserDoesntExistException("User " + username + " doesn't exist.");
        }
        if (!roleRepository.existsByName(roleName)) {
            throw new RoleDoesntExistException("Role " + roleName + "doesn't exist.");
        }

        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        user.setRole(role);
        return mapper.buildUserDTO(user);
    }

    @Override
    public UserDTO banUser(String username) {
        User user = findUser(username);
        user.setBlocked(true);
        return mapper.buildUserDTO(user);
    }

    @Override
    public UserDTO unbanUser(String username) {
        User user = findUser(username);
        user.setBlocked(false);
        return mapper.buildUserDTO(user);
    }

    @Override
    public UserDTO findUserDTO(String username) {
        User user = findUser(username);
        return mapper.buildUserDTO(user);
    }
}
