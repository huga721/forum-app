package huberts.spring.forumapp.topic.dto;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.like.dto.LikeListDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TopicDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String categoryName;
    private boolean closed;
    private LocalDateTime createdTime;
    private LocalDateTime lastEdit;
    private List<LikeListDTO> likes;
    private List<CommentDTO> comments;
}