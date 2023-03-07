package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.topic.dto.*;

import java.util.List;

public interface TopicServiceApi {
    TopicDTO saveNewTopic(TopicCreateDTO topic, String username);
    List<TopicDTO> findAll();
    List<TopicDTO> findAllByTitle(String title);
    TopicDTO findById(Long id);
    TopicDTO changeContent(TopicContentDTO topicDto, String username, Long id);
    TopicDTO changeContentByModerator(TopicEditDTO topicEditDTO, Long id);
    TopicDTO changeTopicCategory(CategoryTitleDTO categoryTitleDTO, Long id);
    void deleteTopic(String username, Long id);
    void deleteTopicByModerator(Long id);
}
