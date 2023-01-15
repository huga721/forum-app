package huberts.spring.forumapp.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    // TODO: Later make this endpoint ONLY FOR ADMIN
    @GetMapping("/getAllUsers")
    ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/deleteUser")
    ResponseEntity<User> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        log.info(username);
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}

