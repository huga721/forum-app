package huberts.spring.forumapp.user;

import huberts.spring.forumapp.exception.UserAlreadyExistingException;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.role.RoleRepository;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserServiceApi{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDTO addUser(RegisterDTO user) {
        String username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistingException("User " + username + " already existing in database.");
        }
        String password = user.getPassword();
        Role roleUser = roleRepository.findByName("ROLE_USER");

        User userCreated = userBuilder(username, password, roleUser);
        userRepository.save(userCreated);

        UserDTO userDTO = userDTOBuilder(username);
        return userDTO;
    }

    @Override
    public Optional<User> findUser(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUser(String username) {
        var user = findUser(username).get();
        userRepository.delete(user);
    }

    private static UserDTO userDTOBuilder(String username) {
        return UserDTO.builder()
                .username(username)
                .build();
    }

    private static User userBuilder(String username, String password, Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return User.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();
    }

}
