package huberts.spring.forumapp.user.dto;

import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserDTO(
        String username,
        String role,
        List<ShortTopicDTO> topics,
        Integer warningPoints,
        boolean blocked,
        LocalDateTime createdTime,
        LocalDateTime lastActivity) {
}