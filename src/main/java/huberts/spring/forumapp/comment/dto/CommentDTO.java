package huberts.spring.forumapp.comment.dto;

import huberts.spring.forumapp.like.dto.LikeListDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int reportsReceived;
    private List<LikeListDTO> likes;
}
