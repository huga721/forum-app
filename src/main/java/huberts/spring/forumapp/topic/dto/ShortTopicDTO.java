package huberts.spring.forumapp.topic.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShortTopicDTO {
    private Long id;
    private String topicTitle;
    private String topicContent;
    private String author;
    private int likes;
    private int comments;
}