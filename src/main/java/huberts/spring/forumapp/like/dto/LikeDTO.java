package huberts.spring.forumapp.like.dto;

import lombok.Builder;

@Builder
public record LikeDTO(
        Long id,
        String who,
        String likedObject,
        Long likedObjectId) {
}