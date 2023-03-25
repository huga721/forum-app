package huberts.spring.forumapp.report.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReportDTO {
    private Long id;
    private String reason;
    private boolean seen;
    private String whoReported;
    private String reportedObject;
    private Long objectId;
}