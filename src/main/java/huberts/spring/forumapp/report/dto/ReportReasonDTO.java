package huberts.spring.forumapp.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportReasonDTO {
    @NotBlank(message = "Reason of report can't be blank.")
    private String reason;
}