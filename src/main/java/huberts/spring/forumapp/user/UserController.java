package huberts.spring.forumapp.user;

import huberts.spring.forumapp.security.annotation.AdminRole;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/stuff")
    ResponseEntity<List<UserDTO>> getStuffMembers() {
        return ResponseEntity.ok(userService.findAllStaffUsers());
    }

    @GetMapping("/{username}")
    ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserDTO(username));
    }

    @UserRole
    @GetMapping("/user/profile")
    ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.currentUser(username));
    }

    @UserRole
    @PatchMapping("/user/change-password")
    ResponseEntity<UserDTO> changePassword(Authentication authentication, @RequestBody PasswordDTO password) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.changePassword(password, username));
    }

    @UserRole
    @DeleteMapping("/user/delete-account")
    ResponseEntity<Void> deleteAccount(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @ModeratorRole
    @GetMapping("/moderator/get-all")
    ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @ModeratorRole
    @PatchMapping("/moderator/users/{username}/ban")
    ResponseEntity<UserDTO> banUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.banUser(username));
    }

    @ModeratorRole
    @PatchMapping("/moderator/users/{username}/unban")
    ResponseEntity<UserDTO> unbanUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.unbanUser(username));
    }

    @ModeratorRole
    @PatchMapping("/moderator/users/{username}/password")
    ResponseEntity<UserDTO> changeUserPassword(@PathVariable String username, @RequestBody PasswordDTO password) {
        return ResponseEntity.ok(userService.changePassword(password, username));
    }

    @AdminRole
    @DeleteMapping("/admin/users/{username}/delete")
    ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @AdminRole
    @PatchMapping("/admin/users/{username}/change-role/{roleName}")
    ResponseEntity<UserDTO> changeRole(@PathVariable String username, @PathVariable String roleName) {
        return ResponseEntity.ok(userService.changeRole(username, roleName));
    }
}

