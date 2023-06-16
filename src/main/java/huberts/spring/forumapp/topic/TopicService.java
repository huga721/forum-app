package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.category.CategoryRepository;
import huberts.spring.forumapp.category.dto.UpdateTopicCategoryDTO;
import huberts.spring.forumapp.comment.CommentService;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicAlreadyExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.topic.dto.*;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.utility.UtilityService;
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

    private static final String CATEGORY_DOESNT_EXIST_EXCEPTION = "Category with title \"%s\" doesn't exist.";
    private static final String TOPIC_DUPLICATE_EXCEPTION = "Topic with title \"%s\" is already created by same user, in the same category, you can't double topics.";
    private static final String TOPIC_ID_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private static final String TOPIC_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION = "There is no topic created with id \"%d\" by current user.";
    private static final String TOPIC_IS_CLOSED_EXCEPTION = "Topic with id \"%d\" is closed.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    private final CategoryRepository categoryRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final UtilityService utilityService;

    @Override
    public TopicDTO createTopic(CreateTopicDTO createTopicDTO, String username) {
        log.info("Creating a topic with title {} in category {}", createTopicDTO.title(), createTopicDTO.category());
        Category category = findCategoryByTitle(createTopicDTO.category());
        User author = userRepository.findByUsername(username).get();

        if (topicRepository.existsByTitleAndUserAndCategory(createTopicDTO.title(), author, category)) {
            String errorMessage = String.format(TOPIC_DUPLICATE_EXCEPTION, createTopicDTO.title());
            log.error(EXCEPTION_OCCURRED, new TopicAlreadyExistException(errorMessage));
            throw new TopicAlreadyExistException(errorMessage);
        }

        utilityService.updateUserLastActivity(username);
        log.info("Topic created");
        return buildAndSaveTopic(createTopicDTO, category, author);
    }

    private TopicDTO buildAndSaveTopic(CreateTopicDTO createTopicDTO, Category category, User user) {
        Topic topicBuilt = TopicMapper.buildTopic(createTopicDTO, category, user);
        topicRepository.save(topicBuilt);
        return TopicMapper.buildTopicDTO(topicBuilt);
    }

    private Category findCategoryByTitle(String title) {
        log.info("Finding category by title {}", title);
        return categoryRepository.findByTitle(title)
                .orElseThrow(() -> {
                    String errorMessage = String.format(CATEGORY_DOESNT_EXIST_EXCEPTION, title);
                    log.error(EXCEPTION_OCCURRED, new CategoryDoesntExistException(errorMessage));
                    return new CategoryDoesntExistException(errorMessage);
                });
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        log.info("Getting all topics");
        return TopicMapper.mapTopicListToTopicDTOList(topicRepository.findAll());
    }

    @Override
    public TopicDTO getTopicById(Long topicId) {
        log.info("Getting a topic with id {}", topicId);
        return TopicMapper.buildTopicDTO(findTopicById(topicId));
    }

    private Topic findTopicById(Long topicId) {
        log.info("Finding topic with id {}", topicId);
        return topicRepository.findById(topicId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_ID_DOESNT_EXIST_EXCEPTION, topicId);
                    log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
                    return new TopicDoesntExistException(errorMessage);
                });
    }

    @Override
    public TopicDTO updateTopicByAuthor(Long topicId, UpdateTopicDTO updateTopicDTO, String username) {
        log.info("Editing topic with id {} by user with username {}", topicId, username);
        User author = userRepository.findByUsername(username).get();
        Topic topicFound = findTopicByUserAndId(author, topicId);

        validateTopicClosed(topicFound);
        utilityService.updateUserLastActivity(username);
        log.info("Topic edited");
        return editTopicAndGetDTO(topicFound, updateTopicDTO.title(), updateTopicDTO.content());
    }

    private void validateTopicClosed(Topic topic) {
        if (topic.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, topic.getId());
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
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

    private Topic findTopicByUserAndId(User user, Long topicId) {
        log.info("Finding topic with id {} created by user {}", topicId, user.getUsername());
        return topicRepository.findByUserAndId(user, topicId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION, topicId);
                    log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
                    return new TopicDoesntExistException(errorMessage);
                });
    }

    @Override
    public TopicDTO updateTopicByModerator(Long topicId, UpdateTopicDTO updateTopicDTO, String moderatorName) {
        log.info("Editing topic with id {} by moderator or admin", topicId);
        Topic topicFound = findTopicById(topicId);

        validateTopicClosed(topicFound);
        utilityService.updateUserLastActivity(moderatorName);
        log.info("Topic edited");
        return editTopicAndGetDTO(topicFound, updateTopicDTO.title(), updateTopicDTO.content());
    }

    @Override
    public TopicDTO changeCategoryOfTopic(Long topicId, UpdateTopicCategoryDTO updateTopicCategoryDTO, String moderatorName) {
        log.info("Changing category of topic with id {}", topicId);
        Category categoryFound = findCategoryByTitle(updateTopicCategoryDTO.categoryTitle());
        Topic topicFound = findTopicById(topicId);

        validateTopicClosed(topicFound);
        utilityService.updateUserLastActivity(moderatorName);
        topicFound.setCategory(categoryFound);
        log.info("Category changed");
        return TopicMapper.buildTopicDTO(topicFound);
    }

    @Override
    public TopicDTO closeTopicByAuthor(Long topicId, CloseReasonDTO closeReasonDTO, String username) {
        log.info("Closing topic with id {} by topic author", topicId);
        CommentContentDTO commentContent = new CommentContentDTO(closeReasonDTO.reason());
        User author = userRepository.findByUsername(username).get();
        Topic topicFound = findTopicByUserAndId(author, topicId);
        return closeAndBuildTopic(topicFound, commentContent);
    }

    private TopicDTO closeAndBuildTopic(Topic topic, CommentContentDTO commentContentDTO) {
        commentService.createComment(topic.getId(), commentContentDTO, topic.getUser().getUsername());
        topic.setClosed(true);
        log.info("Topic closed");
        return TopicMapper.buildTopicDTO(topic);
    }

    @Override
    public TopicDTO closeTopicByModerator(Long topicId, CloseReasonDTO closeReasonDTO, String username) {
        log.info("Closing topic with id {} by moderator or admin", topicId);
        CommentContentDTO commentContent = new CommentContentDTO(closeReasonDTO.reason());
        Topic topicFound = findTopicById(topicId);
        return closeAndBuildTopic(topicFound, commentContent);

    }

    @Override
    public void deleteTopicByAuthor(Long topicId, String username) {
        log.info("Deleting topic with id {} created by user {}", topicId, username);
        User author = userRepository.findByUsername(username).get();
        Topic topicFound = findTopicByUserAndId(author, topicId);
        utilityService.updateUserLastActivity(username);
        topicRepository.delete(topicFound);
        log.info("Topic deleted");
    }

    @Override
    public void deleteTopicByModerator(Long topicId, String moderatorName) {
        log.info("Deleting topic with id {} by moderator or admin", topicId);
        Topic topicFound = findTopicById(topicId);
        utilityService.updateUserLastActivity(moderatorName);
        topicRepository.delete(topicFound);
        log.info("Topic deleted");
    }
}