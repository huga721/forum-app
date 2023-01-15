package huberts.spring.forumapp.security;

import huberts.spring.forumapp.user.UserService;
import huberts.spring.forumapp.user.dto.LoginDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@Slf4j
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;

    @PostMapping("/register")
    ResponseEntity<UserDTO> registerUser (@RequestBody RegisterDTO register) {
        log.info("SecurityController call \'/register\' - endpoint");
        return ResponseEntity.created(URI.create("/" + register.getUsername())).body(userService.addUser(register));
    }
    @PostMapping("/login")
    ResponseEntity<Void> loginUser (@RequestBody LoginDTO credentials) {
        return ResponseEntity.ok().build();
    }
}