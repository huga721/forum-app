package huberts.spring.forumapp.post.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDTO {
    private String title;
    private String content;
    private String category;
}
