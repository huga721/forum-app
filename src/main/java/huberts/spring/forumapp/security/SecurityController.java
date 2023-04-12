package huberts.spring.forumapp.security;

import huberts.spring.forumapp.user.service.UserService;
import huberts.spring.forumapp.user.dto.LoginDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Validated
@RestController
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;

    @PostMapping("/register")
    ResponseEntity<UserDTO> registerUser (@RequestBody @Valid RegisterDTO register) {
        return ResponseEntity.created(URI.create("/" + register.getUsername())).body(userService.createUser(register));
    }
    @PostMapping("/login")
    ResponseEntity<Void> loginUser (@RequestBody LoginDTO credentials) {
        return ResponseEntity.ok().build();
    }
}