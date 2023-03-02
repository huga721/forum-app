package huberts.spring.forumapp.topic.dto;

import lombok.Data;

@Data
public class TopicContentDTO {
    String title;
    String content;
    String category;
}