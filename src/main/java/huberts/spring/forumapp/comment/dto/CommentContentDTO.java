package huberts.spring.forumapp.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentContentDTO {
    @NotBlank(message = "Content of comment can't be blank.")
    private String content;
}