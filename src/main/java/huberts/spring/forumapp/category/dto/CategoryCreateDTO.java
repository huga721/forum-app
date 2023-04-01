package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryCreateDTO {
    @NotBlank(message = "Title is blank.")
    private String title;
    @Size(max = 50)
    private String description;
}
