package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.category.CategoryRepository;
import huberts.spring.forumapp.category.dto.UpdateTopicCategoryDTO;
import huberts.spring.forumapp.comment.CommentService;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicAlreadyExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.topic.dto.CloseReasonDTO;
import huberts.spring.forumapp.topic.dto.CreateTopicDTO;
import huberts.spring.forumapp.topic.dto.TopicDTO;
import huberts.spring.forumapp.topic.dto.UpdateTopicDTO;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    private final static String TITLE = "title of topic";
    private final static String TITLE_TO_CHANGE = "changed title";
    private final static String CONTENT = "information about topic";
    private final static String CONTENT_TO_CHANGE = "changed information about topic";
    private final static String CATEGORY_TITLE = "title of category";
    private final static String CATEGORY_DESCRIPTION = "description of category";
    private final static String CLOSE_REASON = "closing topic because it's monday";
    private final static String USERNAME = "user";
    private final static String EMPTY = "";

    @Mock
    private TopicRepository topicRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentService commentService;
    @InjectMocks
    private TopicService service;

    private Topic topic;
    private Category category;
    private User user;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .title(CATEGORY_TITLE)
                .description(CATEGORY_DESCRIPTION)
                .build();
        user = User.builder()
                .username(USERNAME)
                .build();
        topic = Topic.builder()
                .title(TITLE)
                .content(CONTENT)
                .category(category)
                .user(user)
                .likes(List.of())
                .comments(List.of())
                .build();
    }

    @DisplayName("createTopic method")
    @Nested
    class CreateTopicTests {

        @DisplayName("Should create topic")
        @Test
        void shouldCreateTopic() {
            CreateTopicDTO createDTO = new CreateTopicDTO(TITLE, CONTENT, CATEGORY_TITLE);

            when(categoryRepository.findByTitle(any(String.class))).thenReturn(Optional.of(category));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.existsByTitleAndUserAndCategory(any(String.class), any(User.class),
                    any(Category.class))).thenReturn(false);
            TopicDTO topicCreated = service.createTopic(createDTO, USERNAME);

            Assertions.assertEquals(topicCreated.author(), USERNAME);
            Assertions.assertEquals(topicCreated.title(), TITLE);
            Assertions.assertEquals(topicCreated.categoryName(), CATEGORY_TITLE);
            Assertions.assertEquals(topicCreated.content(), CONTENT);
        }

        @DisplayName("Should throw TopicAlreadyExistException when topic by same author same title already exists in the same category")
        @Test
        void shouldThrowTopicAlreadyExistException_WhenTopicBySameAuthorSameTitleAlreadyExistsInTheSameCategory() {
            CreateTopicDTO createDTO = new CreateTopicDTO(TITLE, CONTENT, CATEGORY_TITLE);

            when(categoryRepository.findByTitle(any(String.class))).thenReturn(Optional.of(category));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.existsByTitleAndUserAndCategory(any(String.class), any(User.class),
                    any(Category.class))).thenReturn(true);

            assertThrows(TopicAlreadyExistException.class, () -> service.createTopic(createDTO, USERNAME));
        }

        @DisplayName("Should throw CategoryDoesntExistException when topic that should be in given category doesn't exist")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenTopicThatShouldBeInGivenCategoryDoesntExist() {
            CreateTopicDTO createDTO = new CreateTopicDTO(TITLE, CONTENT, CATEGORY_TITLE);
            assertThrows(CategoryDoesntExistException.class, () -> service.createTopic(createDTO, USERNAME));
        }
    }

    @DisplayName("getAllTopics method")
    @Nested
    class GetAllTopicsMethod {

        @DisplayName("Should return all topics")
        @Test
        void shouldReturnAllTopics() {
            List<Topic> topics = List.of(topic);
            when(topicRepository.findAll()).thenReturn(topics);
            List<TopicDTO> topicsDTO = service.getAllTopics();

            assertEquals(topicsDTO.size(), topics.size());
        }
    }

    @DisplayName("getTopicById method")
    @Nested
    class GetTopicByIdMethod {

        @DisplayName("Should get topic")
        @Test
        void shouldGetTopic() {
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            TopicDTO topicDTO = service.getTopicById(1L);

            Assertions.assertEquals(topicDTO.title(), topic.getTitle());
            Assertions.assertEquals(topicDTO.content(), topic.getContent());
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() {
            assertThrows(TopicDoesntExistException.class, () -> service.getTopicById(1L));
        }
    }

    @DisplayName("updateTopicByAuthor method")
    @Nested
    class UpdateTopicByAuthorMethod {

        @DisplayName("Should update topic")
        @Test
        void shouldUpdateTopic() {
            UpdateTopicDTO editDTO = new UpdateTopicDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.findByUserAndId(any(User.class), any(Long.class))).thenReturn(Optional.of(topic));
            TopicDTO result = service.updateTopicByAuthor(1L, editDTO, USERNAME);

            assertEquals(result.title(), TITLE_TO_CHANGE);
            assertEquals(result.content(), CONTENT_TO_CHANGE);
        }

        @DisplayName("Should not update topic when edit object is empty")
        @Test
        void shouldNotUpdateTopicWhenEditObjectIsEmpty() {
            UpdateTopicDTO editDTO = new UpdateTopicDTO(EMPTY, EMPTY);

            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.findByUserAndId(any(User.class), any(Long.class))).thenReturn(Optional.of(topic));
            TopicDTO result = service.updateTopicByAuthor(1L, editDTO, USERNAME);

            assertEquals(result.title(), topic.getTitle());
            assertEquals(result.content(), topic.getContent());
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() {
            UpdateTopicDTO editDTO = new UpdateTopicDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(TopicDoesntExistException.class, () -> service.updateTopicByAuthor(1L, editDTO, USERNAME));
        }
    }

    @DisplayName("updateTopicByModerator method")
    @Nested
    class UpdateTopicByModeratorMethod {

        @DisplayName("Should update topic")
        @Test
        void shouldUpdateTopic() {
            UpdateTopicDTO editDTO = new UpdateTopicDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);

            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            TopicDTO result = service.updateTopicByModerator(1L, editDTO, USERNAME);

            assertEquals(result.title(), TITLE_TO_CHANGE);
            Assertions.assertEquals(result.content(), CONTENT_TO_CHANGE);
        }

        @DisplayName("Should not update topic when edit object is empty")
        @Test
        void shouldNotUpdateTopicWhenEditObjectIsEmpty() {
            UpdateTopicDTO editDTO = new UpdateTopicDTO(EMPTY, EMPTY);

            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            TopicDTO result = service.updateTopicByModerator(1L, editDTO, USERNAME);

            assertEquals(result.title(), topic.getTitle());
            assertEquals(result.content(), topic.getContent());
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() {
            UpdateTopicDTO editDTO = new UpdateTopicDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            assertThrows(TopicDoesntExistException.class, () -> service.updateTopicByModerator(1L, editDTO, USERNAME));
        }
    }

    @DisplayName("changeCategoryOfTopic method")
    @Nested
    class ChangeCategoryOfTopicMethod {

        private Category categoryToChange;

        @BeforeEach
        void setUp() {
            categoryToChange = Category.builder()
                    .title(TITLE_TO_CHANGE)
                    .description(CATEGORY_DESCRIPTION)
                    .build();
        }

        @DisplayName("Should change category")
        @Test
        void shouldChangeCategory() {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(TITLE_TO_CHANGE);
            when(categoryRepository.findByTitle(any(String.class))).thenReturn(Optional.of(categoryToChange));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            TopicDTO result = service.changeCategoryOfTopic(1L, titleDTO, USERNAME);

            assertEquals(result.categoryName(), TITLE_TO_CHANGE);
        }

        @DisplayName("Should throw CategoryDoesntExistException when category to change doesn't exist")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenCategoryToChangeDoesntExist() {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(TITLE_TO_CHANGE);
            assertThrows(CategoryDoesntExistException.class, () -> service.changeCategoryOfTopic(1L, titleDTO, USERNAME));
        }

        @DisplayName("Should throw TopicDoesntExistException when given topic doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenGivenTopicDoesntExist() {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(TITLE_TO_CHANGE);
            when(categoryRepository.findByTitle(any(String.class))).thenReturn(Optional.of(categoryToChange));
            assertThrows(TopicDoesntExistException.class, () -> service.changeCategoryOfTopic(1L, titleDTO, USERNAME));
        }
    }

    @DisplayName("closeTopicByAuthor method")
    @Nested
    class CloseTopicByAuthorMethod {

        @DisplayName("Should close topic")
        @Test
        void shouldCloseTopic() {
            CloseReasonDTO closeTopicDTO = new CloseReasonDTO(CLOSE_REASON);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.findByUserAndId(any(User.class), any(Long.class))).thenReturn(Optional.of(topic));
            TopicDTO result = service.closeTopicByAuthor(1L, closeTopicDTO, USERNAME);
            assertTrue(result.closed());
        }

        @DisplayName("Should throw TopicDoesntExistException when can't find topic with given id")
        @Test
        void shouldThrowTopicDoesntExistException_WhenCantFindTopicWithGivenId() {
            CloseReasonDTO closeTopicDTO = new CloseReasonDTO(CLOSE_REASON);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user ));
            assertThrows(TopicDoesntExistException.class, () -> service.closeTopicByAuthor(1L, closeTopicDTO, USERNAME));
        }
    }

    @DisplayName("closeTopicByModerator method")
    @Nested
    class CloseTopicByModeratorMethod {

        @DisplayName("Should close topic")
        @Test
        void shouldCloseTopic() {
            CloseReasonDTO closeTopicDTO = new CloseReasonDTO(CLOSE_REASON);
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            TopicDTO result = service.closeTopicByModerator(1L, closeTopicDTO, USERNAME);
            assertTrue(result.closed());
        }

        @DisplayName("Should throw TopicDoesntExistException when can't find topic with given id")
        @Test
        void shouldThrowTopicDoesntExistException_WhenCantFindTopicWithGivenId() {
            CloseReasonDTO closeTopicDTO = new CloseReasonDTO(CLOSE_REASON);
            assertThrows(TopicDoesntExistException.class, () ->service.closeTopicByModerator(1L, closeTopicDTO, USERNAME));
        }
    }

    @DisplayName("deleteTopicByAuthor method")
    @Nested
    class DeleteTopicByAuthorMethod {

        @DisplayName("Should delete topic")
        @Test
        void shouldDeleteTopic() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(topicRepository.findByUserAndId(any(User.class), any(Long.class))).thenReturn(Optional.of(topic));
            service.deleteTopicByAuthor(1L, USERNAME);
            verify(topicRepository, times(1)).delete(topic);
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(TopicDoesntExistException.class, () -> service.deleteTopicByAuthor(1L, USERNAME));
        }
    }

    @DisplayName("deleteTopicByModerator method")
    @Nested
    class DeleteTopicByModeratorMethod {

        @DisplayName("Should delete topic")
        @Test
        void shouldDeleteTopic() {
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            service.deleteTopicByModerator(1L, USERNAME);
            verify(topicRepository, times(1)).delete(topic);
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() {
            assertThrows(TopicDoesntExistException.class, () -> service.deleteTopicByModerator(1L, USERNAME));
        }
    }
}