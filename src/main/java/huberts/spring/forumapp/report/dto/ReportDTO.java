package huberts.spring.forumapp.report.dto;

import lombok.Builder;

@Builder
public record ReportDTO(
        Long id,
        String reason,
        boolean seen,
        String whoReported,
        String reportedObject,
        Long objectId) {
}