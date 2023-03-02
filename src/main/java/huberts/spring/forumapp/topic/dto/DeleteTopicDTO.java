package huberts.spring.forumapp.topic.dto;

import lombok.Data;

@Data
public class DeleteTopicDTO {
    private String title;
    private String category;
}