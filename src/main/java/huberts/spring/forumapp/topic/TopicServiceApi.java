package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.dto.UpdateTopicCategoryDTO;
import huberts.spring.forumapp.topic.dto.*;

import java.util.List;

public interface TopicServiceApi {
    TopicDTO createTopic(CreateTopicDTO createTopicDTO, String username);

    TopicDTO getTopicById(Long topicId);
    List<TopicDTO> getAllTopics();

    TopicDTO updateTopicByAuthor(Long topicId, UpdateTopicDTO updateTopicDTO, String username);
    TopicDTO updateTopicByModerator(Long topicId, UpdateTopicDTO updateTopicDTO, String moderatorName);
    TopicDTO changeCategoryOfTopic(Long topicId, UpdateTopicCategoryDTO updateTopicCategoryDTO, String moderatorName);
    TopicDTO closeTopicByAuthor(Long topicId, CloseReasonDTO closeReasonDTO, String username);
    TopicDTO closeTopicByModerator(Long topicId, CloseReasonDTO closeReasonDTO, String moderatorName);

    void deleteTopicByAuthor(Long topicId, String username);
    void deleteTopicByModerator(Long topicId, String moderatorName);
}