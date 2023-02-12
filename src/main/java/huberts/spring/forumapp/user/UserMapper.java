package huberts.spring.forumapp.user;

import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.user.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserMapper {

    User mapFromCredentials (String username, String password, Role role) {
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .blocked(false)
                .build();
    }

    List<UserDTO> mapFromList (List<User> listOfUser) {
        return listOfUser.stream()
                .map(this::mapFromUser)
                .collect(Collectors.toList());
    }

    UserDTO mapFromUser (User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .role(user.getRole().getName())
                .postActivity(user.getPosts())
                .blocked(user.isBlocked())
                .build();
    }
}