package huberts.spring.forumapp.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "Username can't be blank.")
    private String username;
    @NotBlank(message = "Password can't be blank.")
    private String password;
}
