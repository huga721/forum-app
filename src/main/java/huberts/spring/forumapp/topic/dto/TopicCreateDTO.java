package huberts.spring.forumapp.topic.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicCreateDTO {
    @NotBlank(message = "Topic title can't be blank.")
    private String title;
    @NotBlank(message = "Topic content can't be blank.")
    private String content;
    @NotBlank(message = "Category can't be blank.")
    private String category;
}