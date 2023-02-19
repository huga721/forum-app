package huberts.spring.forumapp.user;

import huberts.spring.forumapp.security.annotation.AdminRole;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
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
    ResponseEntity<List<UserDTO>> getStaffMembers() {
        return ResponseEntity.ok(userService.findAllStaffUsers());
    }

    @GetMapping("/{username}")
    ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserDTO(username));
    }

    @UserRole
    @GetMapping("/profile")
    ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.currentUser(username));
    }

    @UserRole
    @DeleteMapping("/user/delete/account")
    ResponseEntity<Void> deleteAccount(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @ModeratorRole
    @GetMapping("/stuff/get/all")
    ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @ModeratorRole
    @PatchMapping("/stuff/ban/{username}")
    ResponseEntity<UserDTO> banUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.banUser(username));
    }

    @ModeratorRole
    @PatchMapping("/stuff/unban/{username}")
    ResponseEntity<UserDTO> unbanUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.unbanUser(username));
    }

    @AdminRole
    @DeleteMapping("/stuff/delete/{username}")
    ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok().build();
    }

    @AdminRole
    @PatchMapping("/stuff/change/role/{username}/to/{roleName}")
    ResponseEntity<UserDTO> changeRole(@PathVariable String username, @PathVariable String roleName) {
        return ResponseEntity.ok(userService.changeRole(username, roleName));
    }
}

