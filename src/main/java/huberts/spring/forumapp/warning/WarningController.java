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
    ResponseEntity<List<WarningDTO>> getAll() {
        return ResponseEntity.ok(service.getAllWarnings());
    }

    @ModeratorRole
    @GetMapping("/{id}")
    ResponseEntity<WarningDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getWarningById(id));
    }

    @ModeratorRole
    @PostMapping("/{username}")
    ResponseEntity<WarningDTO> giveWarning(@PathVariable String username) {
        return ResponseEntity.ok(service.giveWarning(username));
    }

    @ModeratorRole
    @DeleteMapping("/{username}")
    ResponseEntity<WarningDTO> deleteWarning(@PathVariable String username) {
        service.deleteWarning(username);
        return ResponseEntity.noContent().build();
    }
}