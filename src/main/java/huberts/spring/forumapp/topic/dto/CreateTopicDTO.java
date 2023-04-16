package huberts.spring.forumapp.topic.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTopicDTO(
        @NotBlank(message = "Topic title can't be blank.") String title,
        @NotBlank(message = "Topic content can't be blank.") String content,
        @NotBlank(message = "Category can't be blank.") String category) {
}