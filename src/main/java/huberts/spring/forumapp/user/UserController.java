package huberts.spring.forumapp.user;

import huberts.spring.forumapp.security.annotation.AdminRole;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import huberts.spring.forumapp.user.dto.PasswordDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Get all users")
    @GetMapping
    List<UserDTO> getAllUsers() {
        List<UserDTO> users = service.getAllUsers();
        return users;
    }

    @Operation(summary = "Get all team users")
    @GetMapping("/team")
    List<UserDTO> getAllModeratorAndAdminUsers() {
        List<UserDTO> teamUsers = service.getAllModeratorAndAdminUsers();
        return teamUsers;
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/{userId}")
    UserDTO getUserById(@PathVariable Long userId) {
        UserDTO user = service.getUserById(userId);
        return user;
    }

    @UserRole
    @Operation(summary = "[USER] Get authenticated user")
    @GetMapping("/profile")
    UserDTO getCurrentUser(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        UserDTO user = service.getUserByUsername(username);
        return user;
    }

    @UserRole
    @Operation(summary = "[USER] Change authenticated user password")
    @PatchMapping("/change-password")
    UserDTO changePassword(@Valid @RequestBody PasswordDTO password,
                                           Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        UserDTO user = service.changePasswordByUsername(password, username);
        return user;
    }

    @UserRole
    @Operation(summary = "[USER] Delete authenticated user")
    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteUserById(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Ban user by id")
    @PatchMapping("/moderator/ban/{userId}")
    UserDTO banUser(@PathVariable Long userId) {
        UserDTO user = service.banUserById(userId);
        return user;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Unban user by id")
    @PatchMapping("/moderator/unban/{userId}")
    UserDTO unbanUser(@PathVariable Long userId) {
        UserDTO user = service.unbanUserById(userId);
        return user;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Change password of user by id")
    @PatchMapping("/moderator/change-password/{userId}")
    UserDTO changePassword(@PathVariable Long userId, @Valid @RequestBody PasswordDTO password) {
        UserDTO user = service.changePasswordById(password, userId);
        return user;
    }

    @AdminRole
    @Operation(summary = "[ADMIN] Delete user by id")
    @DeleteMapping("/admin/delete/{userId}")
    ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
        service.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @AdminRole
    @Operation(summary = "[ADMIN] Change role of user by id")
    @PatchMapping("/admin/edit/{userId}/role/{roleName}")
    UserDTO changeRole(@PathVariable Long userId, @PathVariable String roleName) {
        UserDTO user = service.changeRoleById(userId, roleName);
        return user;
    }
}