package huberts.spring.forumapp.user;

import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.CredentialsDTO;
import huberts.spring.forumapp.user.dto.UserDTO;

import java.util.List;

public interface UserServiceApi {
    UserDTO createUser(CredentialsDTO credentialsDTO);

    UserDTO getUserById(Long userId);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    List<UserDTO> getAllModeratorAndAdminUsers();

    UserDTO changeRoleById(Long userId, String roleName);
    UserDTO changePasswordById(PasswordDTO passwordDTO, Long userId);
    UserDTO changePasswordByUsername(PasswordDTO passwordDTO, String username);
    UserDTO banUserById(Long userId);
    UserDTO unbanUserById(Long userId);

    void deleteUserById(Long userId);
    void deleteUserByUsername(String username);
}