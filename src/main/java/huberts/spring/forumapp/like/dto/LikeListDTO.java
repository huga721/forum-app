package huberts.spring.forumapp.like.dto;

import lombok.Builder;

@Builder
public record LikeListDTO(
        Long id,
        String who) {
}