package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryTitleDTO {
    @NotBlank(message = "Title to change can't be blank.")
    private String categoryTitle;
}