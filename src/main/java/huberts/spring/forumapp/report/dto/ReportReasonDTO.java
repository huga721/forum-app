package huberts.spring.forumapp.report.dto;

import jakarta.validation.constraints.NotBlank;

public record ReportReasonDTO(
        @NotBlank(message = "Reason of report can't be blank.") String reason) {
}