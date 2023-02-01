package huberts.spring.forumapp.post.dto;

import huberts.spring.forumapp.category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String title;
    private String content;
    private String author;
    private String category;
}
