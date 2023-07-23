package huberts.spring.forumapp.security;

import huberts.spring.forumapp.user.UserService;
import huberts.spring.forumapp.user.dto.LoginDTO;
import huberts.spring.forumapp.user.dto.CredentialsDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;

    @Operation(summary = "Register user")
    @PostMapping("/register")
    ResponseEntity<UserDTO> registerUser (@RequestBody @Valid CredentialsDTO credentialsDTO) {
        UserDTO user = userService.createUser(credentialsDTO);
        return ResponseEntity.created(URI.create("/" + credentialsDTO.username())).body(user);
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    Void loginUser (@RequestBody LoginDTO credentials) {
        return null;
    }
}