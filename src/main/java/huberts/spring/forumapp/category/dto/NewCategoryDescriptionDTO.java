package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;

public record NewCategoryDescriptionDTO(
        @NotBlank(message = "Description to change can't be blank.") String description) {
}