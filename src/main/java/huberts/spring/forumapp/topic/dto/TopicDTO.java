package huberts.spring.forumapp.topic.dto;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.like.dto.LikeListDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TopicDTO(
        Long id,
        String title,
        String content,
        String author,
        String categoryName,
        boolean closed,
        LocalDateTime createdTime,
        LocalDateTime lastEdit,
        List<LikeListDTO> likes,
        List<CommentDTO> comments) {
}