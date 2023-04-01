package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDescriptionDTO {
    @NotBlank(message = "Category description is blank.")
    private String description;
}