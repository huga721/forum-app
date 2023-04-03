package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.category.CategoryRepository;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.comment.CommentService;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicAlreadyExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.topic.dto.*;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TopicService implements TopicServiceApi {

    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    private static final String CATEGORY_DOESNT_EXIST_EXCEPTION = "Category with title \"%s\" doesn't exist.";
    private static final String TOPIC_DUPLICATE_EXCEPTION = "Topic with title \"%s\" is already created by same user, in the same category, you can't double topics.";
    private static final String TOPIC_ID_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private static final String TOPIC_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION = "There is no topic created with id \"%d\" by current user.";
    private static final String TOPIC_IS_CLOSED_EXCEPTION = "Topic with id \"%d\" is closed.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    @Override
    public TopicDTO createTopic(TopicCreateDTO topic, String username) {
        String topicCategory = topic.getCategory();
        String title = topic.getTitle();
        log.info("Creating a topic with title {} in category {}", title, topicCategory);

        Category category = findCategoryByTitle(topicCategory);
        User author = userRepository.findByUsername(username).get();

        if (topicRepository.existsByTitleAndUserAndCategory(title, author, category)) {
            String errorMessage = String.format(TOPIC_DUPLICATE_EXCEPTION, title);
            log.error(EXCEPTION_OCCURRED, new TopicAlreadyExistException(errorMessage));
            throw new TopicAlreadyExistException(errorMessage);
        }

        Topic builtTopic = TopicMapper.buildNewTopic(topic, category, author);
        topicRepository.save(builtTopic);

        author.setLastActivity(LocalDateTime.now());
        log.info("Topic created");
        return TopicMapper.buildTopicDTO(builtTopic);
    }

    private Category findCategoryByTitle(String title) {
        log.info("Finding category by title {}", title);
        return categoryRepository.findByTitle(title)
                .orElseThrow(() -> {
                    String errorMessage = String.format(CATEGORY_DOESNT_EXIST_EXCEPTION, title);
                    log.error(EXCEPTION_OCCURRED, new CategoryDoesntExistException(errorMessage));
                    throw new CategoryDoesntExistException(errorMessage);
                });
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        log.info("Getting all topics");
        return TopicMapper.mapFromListTopic(topicRepository.findAll());
    }

    @Override
    public TopicDTO getTopicById(Long id) {
        log.info("Getting a topic with id {}", id);
        return TopicMapper.buildTopicDTO(findTopicById(id));
    }

    private Topic findTopicById(Long id) {
        log.info("Finding topic with id {}", id);
        return topicRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_ID_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
                    throw new TopicDoesntExistException(errorMessage);
                });
    }

    @Override
    public TopicDTO updateTopicByAuthor(Long id, TopicEditDTO topicEditDTO, String username) {
        log.info("Editing topic with id {} by user with username {}", id, username);
        User currentUser = userRepository.findByUsername(username).get();
        Topic topicFound = findTopicByUserAndId(currentUser, id);
        if (topicFound.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
        currentUser.setLastActivity(LocalDateTime.now());

        String content = topicEditDTO.getContent();
        String title = topicEditDTO.getTitle();
        log.info("Topic edited");
        return editTopicAndGetDTO(topicFound, title, content);
    }

    private TopicDTO editTopicAndGetDTO(Topic topicFound, String title, String content) {
        boolean isUpdated = false;
        if (!title.isBlank()) {
            log.info("Changed title of topic");
            topicFound.setTitle(title);
            isUpdated = true;
        }
        if (!content.isBlank()) {
            log.info("Changed content of topic");
            topicFound.setContent(content);
            isUpdated = true;
        }
        if (isUpdated) {
            topicFound.setLastEdit(LocalDateTime.now());
        }
        return TopicMapper.buildTopicDTO(topicFound);
    }

    private Topic findTopicByUserAndId(User user, Long id) {
        log.info("Finding topic with id {} created by user {}", id, user.getUsername());
        return topicRepository.findByUserAndId(user, id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
                    return new TopicDoesntExistException(errorMessage);
                });
    }

    @Override
    public TopicDTO updateTopicByModerator(Long id, TopicEditDTO topicDto, String moderatorName) {
        log.info("Editing topic with id {} by moderator or admin", id);
        Topic topicFound = findTopicById(id);
        if (topicFound.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }

        String title = topicDto.getTitle();
        String content = topicDto.getContent();

        updateModeratorLastActivity(moderatorName);
        log.info("Topic edited");
        return editTopicAndGetDTO(topicFound, title, content);
    }

    private void updateModeratorLastActivity(String username) {
        User moderator = userRepository.findByUsername(username).get();
        moderator.setLastActivity(LocalDateTime.now());
    }

    @Override
    public TopicDTO changeCategoryOfTopic(Long id, CategoryTitleDTO categoryTitleDTO, String moderatorName) {
        log.info("Changing category of topic with id {}", id);
        String categoryTitle = categoryTitleDTO.getCategoryTitle();

        Category categoryResult = findCategoryByTitle(categoryTitle);
        Topic topicFound = findTopicById(id);
        if (topicFound.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }

        topicFound.setCategory(categoryResult);
        updateModeratorLastActivity(moderatorName);
        log.info("Category changed");
        return TopicMapper.buildTopicDTO(topicFound);
    }

    @Override
    public TopicDTO closeTopicByAuthor(Long id, CloseTopicDTO closeTopicDTO, String username) {
        log.info("Closing topic with id {} by topic author", id);
        String closeReason = closeTopicDTO.getReason();
        CommentContentDTO commentContent = new CommentContentDTO(closeReason);

        User author = userRepository.findByUsername(username).get();
        Topic topicFound = findTopicByUserAndId(author, id);
        closeTopic(topicFound, commentContent);
        return TopicMapper.buildTopicDTO(topicFound);
    }

    private void closeTopic(Topic topic, CommentContentDTO commentContent) {
        commentService.createComment(topic.getId(), commentContent, topic.getUser().getUsername());
        topic.setClosed(true);
        log.info("Topic closed");
    }

    @Override
    public TopicDTO closeTopicByModerator(Long id, CloseTopicDTO closeTopicDTO, String username) {
        log.info("Closing topic with id {} by moderator or admin", id);
        String closeReason = closeTopicDTO.getReason();
        CommentContentDTO commentContent = new CommentContentDTO(closeReason);

        Topic topicFound = findTopicById(id);
        closeTopic(topicFound, commentContent);
        return TopicMapper.buildTopicDTO(topicFound);
    }

    @Override
    public void deleteTopicByAuthor(Long id, String username) {
        log.info("Deleting topic with id {} created by user {}", id, username);
        User author = userRepository.findByUsername(username).get();
        Topic topicResult = findTopicByUserAndId(author, id);
        author.setLastActivity(LocalDateTime.now());
        topicRepository.delete(topicResult);
        log.info("Topic deleted");
    }

    @Override
    public void deleteTopicByModerator(Long id, String moderatorName) {
        Topic topicFound = findTopicById(id);
        log.info("Deleting topic with id {} by moderator or admin", id);
        updateModeratorLastActivity(moderatorName);
        topicRepository.delete(topicFound);
        log.info("Topic deleted");
    }
}