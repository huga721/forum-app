package huberts.spring.forumapp.warning.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WarningDTO {
    private Long id;
    private String username;
    private int warningPoints;
    private String status;
}