package huberts.spring.forumapp.category.dto;

import huberts.spring.forumapp.topic.dto.TopicDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryDTO {
    private String title;
    private String description;
    private List<TopicDTO> topics;
}
