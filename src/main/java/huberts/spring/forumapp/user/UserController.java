package huberts.spring.forumapp.user;

import huberts.spring.forumapp.security.annotation.AdminRole;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/staff")
    ResponseEntity<List<UserDTO>> getAllModeratorAndAdminUsers() {
        List<UserDTO> staffMembers = service.getAllModeratorAndAdminUsers();
        return ResponseEntity.ok(staffMembers);
    }

    @GetMapping("/{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = service.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @UserRole
    @GetMapping("/profile")
    ResponseEntity<UserDTO> getCurrentUser(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        UserDTO user = service.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @UserRole
    @PatchMapping("/change-password")
    ResponseEntity<UserDTO> changePassword(@Valid @RequestBody PasswordDTO password,
                                           Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        UserDTO user = service.changePassword(password, username);
        return ResponseEntity.ok(user);
    }

    @UserRole
    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteUserByUsername(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @ModeratorRole
    @GetMapping("/moderator/all")
    ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @ModeratorRole
    @PatchMapping("/moderator/ban/{username}")
    ResponseEntity<UserDTO> banUser(@PathVariable String username) {
        UserDTO user = service.banUser(username);
        return ResponseEntity.ok(user);
    }

    @ModeratorRole
    @PatchMapping("/moderator/unban/{username}")
    ResponseEntity<UserDTO> unbanUser(@PathVariable String username) {
        UserDTO user = service.unbanUser(username);
        return ResponseEntity.ok(user);
    }

    @ModeratorRole
    @PatchMapping("/moderator/change-password/{username}")
    ResponseEntity<UserDTO> changePassword(@PathVariable String username,
                                               @Valid @RequestBody PasswordDTO password) {
        UserDTO user = service.changePassword(password, username);
        return ResponseEntity.ok(user);
    }

    @AdminRole
    @DeleteMapping("/admin/delete/{username}")
    ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        service.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @AdminRole
    @PatchMapping("/admin/edit/{username}/role/{roleName}")
    ResponseEntity<UserDTO> changeRole(@PathVariable String username, @PathVariable String roleName) {
        UserDTO user = service.changeRole(username, roleName);
        return ResponseEntity.ok(user);
    }
}