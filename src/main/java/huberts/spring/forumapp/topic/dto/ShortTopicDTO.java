package huberts.spring.forumapp.topic.dto;

import lombok.Builder;

@Builder
public record ShortTopicDTO(
        Long id,
        String topicTitle,
        String topicContent,
        String author,
        Integer likes,
        Integer comments) {
}