package huberts.spring.forumapp.user;

import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserServiceApi {
    UserDTO addUser(RegisterDTO user);

    Optional<User> findUser(String name);

    List<User> findAll();
}
