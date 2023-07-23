package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.warning.dto.WarningDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/warnings")
public class WarningController {

    private final WarningService service;

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Get all warnings")
    @GetMapping()
    List<WarningDTO> getAllWarnings() {
        List<WarningDTO> warnings = service.getAllWarnings();
        return warnings;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Get warning by id")
    @GetMapping("/{warningId}")
    WarningDTO getWarningById(@PathVariable Long warningId) {
        WarningDTO warning = service.getWarningById(warningId);
        return warning;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Create warning")
    @PostMapping("/{userId}")
    ResponseEntity<WarningDTO> createWarning(@PathVariable Long userId) {
        WarningDTO warning = service.createWarning(userId);
        return ResponseEntity.created(URI.create("/warnings")).body(warning);
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Delete warning by user id")
    @DeleteMapping("/{userId}")
    ResponseEntity<WarningDTO> deleteWarning(@PathVariable Long userId) {
        service.deleteWarning(userId);
        return ResponseEntity.noContent().build();
    }
}