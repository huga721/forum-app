package huberts.spring.forumapp.comment.dto;

import huberts.spring.forumapp.like.dto.LikeListDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentDTO(
        Long id,
        String content,
        String author,
        LocalDateTime createdAt,
        LocalDateTime lastEdit,
        Integer reportsReceived,
        List<LikeListDTO> likes) {
}