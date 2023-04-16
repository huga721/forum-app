package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryDTO(
        @NotBlank(message = "Title is blank.") String title,
        @Size(max = 50) String description) {
}