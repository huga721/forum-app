package huberts.spring.forumapp.warning.dto;

import lombok.Builder;

@Builder
public record WarningDTO(
        Long id,
        String username,
        Integer warningPoints,
        String status) {
}