package huberts.spring.forumapp.user.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class LoginDTO {
    private String username;
    private String password;
}
