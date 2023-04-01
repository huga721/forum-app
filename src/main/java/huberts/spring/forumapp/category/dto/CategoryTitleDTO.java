package huberts.spring.forumapp.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTitleDTO {
    @NotBlank(message = "Title to change can't be blank.")
    private String categoryTitle;
}