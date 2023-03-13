package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDescriptionDTO {
    @NotBlank(message = "Category description is blank.")
    private String description;
}