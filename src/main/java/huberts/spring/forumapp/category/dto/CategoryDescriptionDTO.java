package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDescriptionDTO {
    @NotBlank(message = "Category description is blank.")
    private String description;
}