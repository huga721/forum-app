package huberts.spring.forumapp.topic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseReasonDTO {
    @NotBlank(message = "Please write reason of closing topic")
    private String reason;
}