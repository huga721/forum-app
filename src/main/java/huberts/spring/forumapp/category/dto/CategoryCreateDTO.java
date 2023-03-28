package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCreateDTO {
    @NotBlank(message = "Title is blank.")
    private String title;
    @Size(max = 50)
    private String description;
}
