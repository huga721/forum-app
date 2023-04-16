package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryTitleDTO(
        @NotBlank(message = "Title to change can't be blank.") String title) {
}
