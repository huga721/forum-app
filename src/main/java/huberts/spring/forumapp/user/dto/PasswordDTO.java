package huberts.spring.forumapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordDTO {
    @NotBlank(message = "Password can't be blank.")
    private String password;
}
