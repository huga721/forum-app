package huberts.spring.forumapp.topic.dto;

import jakarta.validation.constraints.NotBlank;

public record CloseReasonDTO(
        @NotBlank(message = "Please write reason of closing topic") String reason) {
}