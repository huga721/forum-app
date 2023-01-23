package huberts.spring.forumapp.post.dto;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDTO {
    private String title;
    private String content;
    private User author;
    private String category;
}
