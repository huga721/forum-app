package huberts.spring.forumapp.user;

import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicMapper;
import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import huberts.spring.forumapp.user.dto.UserDTO;
import huberts.spring.forumapp.warning.Warning;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public static User buildUser(String username, String password, Role role) {
        List<Topic> emptyList = new ArrayList<>();
        List<Warning> emptyWarnings = new ArrayList<>();
        return User.builder()
                .username(username)
                .password(password)
                .role(role)
                .blocked(false)
                .topics(emptyList)
                .warnings(emptyWarnings)
                .build();
    }

    public static UserDTO buildUserDTO(User user) {
        return UserDTO.builder()
                .username(user.getUsername())
                .role(user.getRole().getName())
                .topics(mapTopicListToShortTopicDTOList(user.getTopics()))
                .warningPoints(user.getWarnings().size() * 20)
                .blocked(user.isBlocked())
                .createdTime(user.getCreatedAt())
                .lastActivity(user.getLastActivity())
                .build();
    }

    public static List<UserDTO> mapUserListToUserDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::buildUserDTO)
                .collect(Collectors.toList());
    }

    private static List<ShortTopicDTO> mapTopicListToShortTopicDTOList(List<Topic> topics) {
        return TopicMapper.mapTopicListToShortTopicDTOList(topics);
    }
}