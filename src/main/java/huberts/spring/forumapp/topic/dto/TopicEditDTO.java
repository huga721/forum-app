package huberts.spring.forumapp.topic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicEditDTO {
    private String title;
    private String content;
}