package huberts.spring.forumapp.user;

import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.CredentialsDTO;
import huberts.spring.forumapp.user.dto.UserDTO;

import java.util.List;

public interface UserServiceApi {
    UserDTO createUser(CredentialsDTO credentialsDTO);

    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    List<UserDTO> getAllModeratorAndAdminUsers();

    UserDTO changeRole(String username, String roleName);
    UserDTO changePassword(PasswordDTO passwordDTO, String username);
    UserDTO banUser(String username);
    UserDTO unbanUser(String username);

    void deleteUserByUsername(String username);
}