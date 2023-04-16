package huberts.spring.forumapp.user.dto;

import lombok.Builder;

@Builder
public record LoginDTO(
        String username,
        String password) {
}