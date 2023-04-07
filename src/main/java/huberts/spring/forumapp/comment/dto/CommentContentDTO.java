package huberts.spring.forumapp.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentContentDTO {
    @NotBlank(message = "Content of comment can't be blank.")
    private String content;
}