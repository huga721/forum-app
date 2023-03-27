package huberts.spring.forumapp.user.service;

import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;

import java.util.List;

public interface UserServiceApi {
    UserDTO createUser(RegisterDTO user);

    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    List<UserDTO> getAllModeratorAndAdminUsers();

    UserDTO changeRole(String username, String roleName);
    UserDTO changePassword(PasswordDTO newPassword, String username);
    UserDTO banUser(String username);
    UserDTO unbanUser(String username);

    void deleteUserByUsername(String username);
}