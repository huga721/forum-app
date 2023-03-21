package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.warning.dto.WarningDTO;

import java.util.List;

public interface WarningServiceApi {
    WarningDTO giveWarning(String username);

    WarningDTO getWarningById(Long id);
    List<WarningDTO> getAllWarnings();

    void deleteWarning(String username);
}