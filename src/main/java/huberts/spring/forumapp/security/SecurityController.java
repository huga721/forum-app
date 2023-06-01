package huberts.spring.forumapp.security;

import huberts.spring.forumapp.user.UserService;
import huberts.spring.forumapp.user.dto.LoginDTO;
import huberts.spring.forumapp.user.dto.CredentialsDTO;
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
    ResponseEntity<UserDTO> registerUser (@RequestBody @Valid CredentialsDTO credentialsDTO) {
        UserDTO user = userService.createUser(credentialsDTO);
        return ResponseEntity.created(URI.create("/" + credentialsDTO.username())).body(user);
    }
    @PostMapping("/login")
    ResponseEntity<Void> loginUser (@RequestBody LoginDTO credentials) {
        return ResponseEntity.ok().build();
    }
}