package huberts.spring.forumapp.user.dto;

import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String username;
    private String role;
    private List<ShortTopicDTO> topics;
    private int warningPoints;
    private boolean blocked;
    private LocalDateTime lastActivity;
}