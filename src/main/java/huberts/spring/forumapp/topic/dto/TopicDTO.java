package huberts.spring.forumapp.topic.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TopicDTO {
    private String title;
    private String content;
    private String author;
    private String categoryName;
    private LocalDateTime createdTime;
    private LocalDateTime lastEdit;
}