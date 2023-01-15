package huberts.spring.forumapp.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPasswordDTO {
    private String username;
    private String password;
}
