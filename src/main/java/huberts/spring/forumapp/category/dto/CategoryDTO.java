package huberts.spring.forumapp.category.dto;

import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryDTO(
        String title,
        String description,
        List<ShortTopicDTO> topics) {
}