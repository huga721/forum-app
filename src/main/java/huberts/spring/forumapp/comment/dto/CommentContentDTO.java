package huberts.spring.forumapp.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentContentDTO(
        @NotBlank(message = "Content of comment can't be blank.") String content) {
}