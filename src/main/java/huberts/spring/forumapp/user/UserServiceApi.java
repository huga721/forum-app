package huberts.spring.forumapp.user;

import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;

import java.util.List;

public interface UserServiceApi {
    UserDTO addUser(RegisterDTO user);
    User findUser(String name);
    List<UserDTO> findAllUsers();
    List<UserDTO> findAllStaffUsers();
    void deleteUserByUsername(String username);
    UserDTO currentUser(String username);
    UserDTO changeRole(String username, String roleName);
    UserDTO banUser(String username);
    UserDTO unbanUser(String username);
    UserDTO findUserDTO(String username);
    UserDTO changePassword(PasswordDTO newPassword, String username);
}
