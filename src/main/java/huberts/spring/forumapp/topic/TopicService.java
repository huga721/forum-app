package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.category.CategoryRepository;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.exception.*;
import huberts.spring.forumapp.topic.dto.*;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicService implements TopicServiceApi {

    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    private final String CATEGORY_DOESNT_EXIST_EXCEPTION = "Category with title \"%s\" doesn't exist.";
    private final String TOPIC_DUPLICATE_EXCEPTION = "Topic with title \"%s\" is already created by same user, in the same category, you can't double topics.";
    private final String TOPIC_ID_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private final String TOPIC_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION = "There is no topic created with id \"%d\" by current user.";

    @Override
    public TopicDTO saveNewTopic(TopicCreateDTO topic, String username) {
        String categoryTitle = topic.getCategory();
        String topicTitle = topic.getTitle();

        Category category = findCategoryByTitle(categoryTitle);
        User author = userRepository.findByUsername(username);

        if (topicRepository.existsByTitleAndUsersAndCategories(topicTitle, author, category)) {
            throw new TopicExistException(String.format(TOPIC_DUPLICATE_EXCEPTION, topicTitle));
        }

        Topic builtTopic = TopicMapper.buildNewTopic(topic, category, author);
        Topic savedTopic = topicRepository.save(builtTopic);

        return TopicMapper.buildTopicDTO(savedTopic);
    }

    private Category findCategoryByTitle(String title) {
        return categoryRepository.findByTitle(title)
                .orElseThrow(() -> new CategoryExistException(String.format(CATEGORY_DOESNT_EXIST_EXCEPTION, title)));
    }

    @Override
    public List<TopicDTO> findAll() {
        return TopicMapper.mapFromListTopic(topicRepository.findAll());
    }

    @Override
    public List<TopicDTO> findAllByTitle(String title) {
        return TopicMapper.mapFromListTopic(topicRepository.findAllByTitle(title));
    }

    @Override
    public TopicDTO findById(Long id) {
        return TopicMapper.buildTopicDTO(findTopicById(id));
    }

    private Topic findTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new TopicExistException(String.format(TOPIC_ID_DOESNT_EXIST_EXCEPTION, id)));
    }

    @Override
    public TopicDTO changeContent(TopicContentDTO topicContentDTO, String username, Long id) {
        User currentUser = userRepository.findByUsername(username);
        Topic topicFound = findTopicByUserAndId(currentUser, id);
        String content = topicContentDTO.getContent();

        topicFound.setContent(content);
        topicFound.setLastEdit(LocalDateTime.now());

        return TopicMapper.buildTopicDTO(topicFound);
    }

    private Topic findTopicByUserAndId(User user, Long id) {
        return topicRepository.findByUsersAndId(user, id)
                .orElseThrow(() -> new TopicExistException(String.format(TOPIC_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION, id)));
    }

    @Override
    public TopicDTO changeContentByModerator(TopicEditDTO topicDto, Long id) {
        Topic topicResult = findTopicById(id);
        String title = topicDto.getTitle();
        String content = topicDto.getContent();

        if (!title.isBlank())
            topicResult.setTitle(title);
        if (!content.isBlank())
            topicResult.setContent(content);

        return TopicMapper.buildTopicDTO(topicResult);
    }

    @Override
    public TopicDTO changeTopicCategory(CategoryTitleDTO categoryTitleDTO, Long id) {
        String categoryTitle = categoryTitleDTO.getCategoryTitle();

        Category categoryResult = findCategoryByTitle(categoryTitle);

        Topic topicResult = findTopicById(id);
        topicResult.setCategories(List.of(categoryResult));

        return TopicMapper.buildTopicDTO(topicResult);
    }

    @Override
    public void deleteTopic(String username, Long id) {
        User currentUser = userRepository.findByUsername(username);
        Topic topicResult = findTopicByUserAndId(currentUser, id);
        topicRepository.delete(topicResult);
    }

    @Override
    public void deleteTopicByModerator(Long id) {
        Topic topicResult = findTopicById(id);
        topicRepository.delete(topicResult);
    }
}