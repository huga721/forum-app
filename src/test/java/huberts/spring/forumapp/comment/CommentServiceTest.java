package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicRepository;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
class CommentServiceTest {

    private final static String TITLE = "title of topic";
    private final static String CONTENT = "information about topic";
    private final static String USERNAME = "user";
    private final static String COMMENT_CONTENT = "i find this topic really interesting";
    private final static String COMMENT_CONTENT_UPDATED = "updated content of this comment";

    @Mock
    private TopicRepository topicRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentService service;

    private Topic topic;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        topic = Topic.builder()
                .title(TITLE)
                .content(CONTENT).build();
        user = User.builder()
                .username(USERNAME).build();
        comment = Comment.builder()
                .content(CONTENT)
                .likes(List.of())
                .user(user).build();
    }

    @DisplayName("createComment method")
    @Nested
    class CreateCommentTests {

        @DisplayName("Should create comment")
        @Test
        void shouldCreateComment() {
            CommentContentDTO commentContent = new CommentContentDTO(COMMENT_CONTENT);
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            CommentDTO commentCreated = service.createComment(1L, commentContent, USERNAME);

            assertEquals(commentCreated.getAuthor(), USERNAME);
            assertEquals(commentCreated.getContent(), COMMENT_CONTENT);
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() {
            CommentContentDTO commentContent = new CommentContentDTO(COMMENT_CONTENT);
            assertThrows(TopicDoesntExistException.class, () -> service.createComment(1L, commentContent, USERNAME));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where comment should be created is closed")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereCommentShouldBeCreatedIsClosed() {
            CommentContentDTO commentContent = new CommentContentDTO(COMMENT_CONTENT);
            topic.setClosed(true);
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(TopicIsClosedException.class, () -> service.createComment(1L, commentContent, USERNAME));
        }
    }

    @DisplayName("getCommentById method")
    @Nested
    class GetCommentByIdTests {

        @DisplayName("Should return comment")
        @Test
        void shouldReturnComment() {
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            CommentDTO commentFound = service.getCommentById(1L);
            assertEquals(commentFound.getContent(), comment.getContent());
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() {
            assertThrows(CommentDoesntExistException.class, () -> service.getCommentById(1L));
        }
    }

    @DisplayName("getAllComments method")
    @Nested
    class GetAllCommentsTests {

        @DisplayName("Should return all comments")
        @Test
        void ShouldReturnAllComments() {
            when(commentRepository.findAll()).thenReturn(List.of(comment));
            List<CommentDTO> comments = service.getAllComments();
            assertEquals(comments.get(0).getContent(), comment.getContent());
        }
    }

    @DisplayName("getAllCommentsByTopicId method")
    @Nested
    class GetAllCommentsByTopicIdTests {

        @DisplayName("Should return all comments by topic id")
        @Test
        void shouldReturnAllCommentsByTopicId() {
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(commentRepository.findAllByTopic(any(Topic.class))).thenReturn(List.of(comment));
            List<CommentDTO> comments = service.getAllCommentsByTopicId(1L);
            assertEquals(comments.get(0).getContent(), comment.getContent());
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesnt exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() {
            assertThrows(TopicDoesntExistException.class, () -> service.getAllCommentsByTopicId(1L));
        }
    }

    @DisplayName("getAllCommentsByUsername method")
    @Nested
    class GetAllCommentsByUsernameTests {

        @DisplayName("Should return all comments by username")
        @Test
        void shouldReturnAllCommentsByTopicId() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(commentRepository.findAllByUser(any(User.class))).thenReturn(List.of(comment));
            List<CommentDTO> comments = service.getAllCommentsByUsername(USERNAME);
            assertEquals(comments.get(0).getContent(), comment.getContent());
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() {
            assertThrows(UserDoesntExistException.class, () -> service.getAllCommentsByUsername(USERNAME));
        }
    }

    @DisplayName("updateCommentByAuthor method")
    @Nested
    class UpdateCommentByAuthorTests {

        @DisplayName("Should update comment")
        @Test
        void shouldUpdateComment() {
            CommentContentDTO content = new CommentContentDTO(COMMENT_CONTENT_UPDATED);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(commentRepository.findByUserAndId(any(User.class), any(Long.class))).thenReturn(Optional.of(comment));
            CommentDTO commentUpdated = service.updateCommentByAuthor(1L, content, USERNAME);
            assertEquals(commentUpdated.getContent(), COMMENT_CONTENT_UPDATED);
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() {
            CommentContentDTO content = new CommentContentDTO(COMMENT_CONTENT_UPDATED);
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(CommentDoesntExistException.class, () -> service.updateCommentByAuthor(1L, content, USERNAME));
        }
    }

    @DisplayName("updateCommentByModerator method")
    @Nested
    class UpdateCommentByModeratorTests {

        @DisplayName("Should update comment")
        @Test
        void shouldUpdateComment() {
            CommentContentDTO content = new CommentContentDTO(COMMENT_CONTENT_UPDATED);
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            CommentDTO commentUpdated = service.updateCommentByModerator(1L, content);
            assertEquals(commentUpdated.getContent(), COMMENT_CONTENT_UPDATED);
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() {
            CommentContentDTO content = new CommentContentDTO(COMMENT_CONTENT_UPDATED);
            assertThrows(CommentDoesntExistException.class, () -> service.updateCommentByModerator(1L, content));
        }
    }

    @DisplayName("deleteCommentByAuthor method")
    @Nested
    class DeleteCommentByAuthorTests {

        @DisplayName("Should delete comment")
        @Test
        void shouldDeleteComment() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(commentRepository.findByUserAndId(any(User.class), any(Long.class))).thenReturn(Optional.of(comment));
            service.deleteCommentByAuthor(1L, USERNAME);
            verify(commentRepository, times(1)).delete(comment);
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(CommentDoesntExistException.class, () -> service.deleteCommentByAuthor(1L, USERNAME));
        }
    }

    @DisplayName("deleteCommentByModerator method")
    @Nested
    class DeleteCommentByModeratorTests {

        @DisplayName("Should delete comment")
        @Test
        void shouldDeleteComment() {
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            service.deleteCommentByModerator(1L);
            verify(commentRepository, times(1)).delete(comment);
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() {
            assertThrows(CommentDoesntExistException.class, () -> service.deleteCommentByModerator(1L));
        }
    }
}