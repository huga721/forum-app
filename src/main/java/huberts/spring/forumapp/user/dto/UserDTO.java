package huberts.spring.forumapp.user.dto;

import huberts.spring.forumapp.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String role;
    private List<Post> postActivity;
    private boolean blocked;
}
