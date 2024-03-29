package huberts.spring.forumapp.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CredentialsDTO(
        @NotBlank(message = "Username can't be blank.") String username,
        @NotBlank(message = "Password can't be blank.") String password) {
}