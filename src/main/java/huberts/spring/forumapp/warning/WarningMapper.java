package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.warning.dto.WarningDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarningMapper {

    public static Warning buildWarning(User user) {
        return Warning.builder()
                .user(user)
                .build();
    }

    public static WarningDTO buildWarningDTO(Warning warning) {
        User user = warning.getUser();
        return WarningDTO.builder()
                .id(warning.getId())
                .username(user.getUsername())
                .warningPoints(user.getWarnings().size() * 20)
                .status(getStatus(user))
                .build();
    }

    private static String getStatus(User user) {
        if (user.getWarnings().size() == 5)
            return "Banned";
        else
            return "Not banned";
    }

    public static List<WarningDTO> mapWarningsToWarningsDTO(List<Warning> warnings) {
        return warnings.stream()
                .map(WarningMapper::buildWarningDTO)
                .collect(Collectors.toList());
    }
}