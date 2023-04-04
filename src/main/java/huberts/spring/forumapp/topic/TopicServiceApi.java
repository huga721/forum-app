package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.topic.dto.*;

import java.util.List;

public interface TopicServiceApi {
    TopicDTO createTopic(TopicCreateDTO topic, String username);

    TopicDTO getTopicById(Long id);
    List<TopicDTO> getAllTopics();

    TopicDTO updateTopicByAuthor(Long id, TopicEditDTO topicEditDTO, String username);
    TopicDTO updateTopicByModerator(Long id, TopicEditDTO topicEditDTO, String moderatorName);
    TopicDTO changeCategoryOfTopic(Long id, CategoryTitleDTO categoryTitleDTO, String moderatorName);
    TopicDTO closeTopicByAuthor(Long id, CloseReasonDTO closeTopicDTO, String username);
    TopicDTO closeTopicByModerator(Long id, CloseReasonDTO closeTopicDTO, String username);

    void deleteTopicByAuthor(Long id, String username);
    void deleteTopicByModerator(Long id, String moderatorName);
}