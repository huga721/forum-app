package huberts.spring.forumapp.user.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordDTO(
        @NotBlank(message = "Password can't be blank.") String password) {
}