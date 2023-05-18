package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.warning.dto.WarningDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warnings")
public class WarningController {

    private final WarningService service;

    @ModeratorRole
    @GetMapping()
    ResponseEntity<List<WarningDTO>> getAllWarnings() {
        List<WarningDTO> warnings = service.getAllWarnings();
        return ResponseEntity.ok(warnings);
    }

    @ModeratorRole
    @GetMapping("/{warningId}")
    ResponseEntity<WarningDTO> getWarningById(@PathVariable Long warningId) {
        WarningDTO warning = service.getWarningById(warningId);
        return ResponseEntity.ok(warning);
    }

    @ModeratorRole
    @PostMapping("/{username}")
    ResponseEntity<WarningDTO> createWarning(@PathVariable String username) {
        WarningDTO warning = service.createWarning(username);
        return ResponseEntity.ok(warning);
    }

    @ModeratorRole
    @DeleteMapping("/{username}")
    ResponseEntity<WarningDTO> deleteWarning(@PathVariable String username) {
        service.deleteWarning(username);
        return ResponseEntity.noContent().build();
    }
}