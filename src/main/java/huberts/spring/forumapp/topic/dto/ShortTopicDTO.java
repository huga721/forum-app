package huberts.spring.forumapp.topic.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShortTopicDTO {
    private String title;
    private String content;
    private String author;
}