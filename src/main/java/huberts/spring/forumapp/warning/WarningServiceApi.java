package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.warning.dto.WarningDTO;

import java.util.List;

public interface WarningServiceApi {
    WarningDTO createWarning(Long userId);

    WarningDTO getWarningById(Long warningId);
    List<WarningDTO> getAllWarnings();

    void deleteWarning(Long userId);
}