package huberts.spring.forumapp.like;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.comment.CommentRepository;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.like.LikeAlreadyExistException;
import huberts.spring.forumapp.exception.like.LikeDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.like.dto.LikeDTO;
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
class LikeServiceTest {

    private final static String TITLE = "title of topic";
    private final static String CONTENT = "information about topic";
    private final static String USERNAME = "user";
    private final static String STRING_TOPIC = "Topic";
    private final static String STRING_COMMENT = "Comment";

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private LikeService service;

    private User user;
    private Like like;
    private Comment comment;
    private Topic topic;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username(USERNAME)
                .build();
        topic = Topic.builder()
                .id(1L)
                .title(TITLE)
                .content(CONTENT)
                .likes(List.of())
                .comments(List.of())
                .build();
        comment = Comment.builder()
                .id(1L)
                .content(CONTENT)
                .likes(List.of())
                .topic(topic)
                .user(user).build();
        like = Like.builder()
                .user(user)
                .comment(comment)
                .build();
    }

    @DisplayName("createTopicLike method")
    @Nested
    class CreateTopicLikeTests {

        @DisplayName("Should create like in topic")
        @Test
        void shouldCreateLikeInTopic() {
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(likeRepository.existsByTopicAndUser(any(Topic.class), any(User.class))).thenReturn(false);

            LikeDTO likeCreated = service.createTopicLike(1L, USERNAME);

            assertEquals(likeCreated.who(), USERNAME);
            assertEquals(likeCreated.likedObjectId(), 1L);
            assertEquals(likeCreated.likedObject(), STRING_TOPIC);
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() {
            assertThrows(TopicDoesntExistException.class, () -> service.createTopicLike(1L, USERNAME));
        }

        @DisplayName("Should throw LikeAlreadyExistException when like already exists")
        @Test
        void shouldThrowLikeExistException_WhenLikeAlreadyExists() {
            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(likeRepository.existsByTopicAndUser(any(Topic.class), any(User.class))).thenReturn(true);

            assertThrows(LikeAlreadyExistException.class, () -> service.createTopicLike(1L, USERNAME));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where like should be created is closed")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereCommentShouldBeCreatedIsClosed() {
            topic.setClosed(true);

            when(topicRepository.findById(any(Long.class))).thenReturn(Optional.of(topic));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

            assertThrows(TopicIsClosedException.class, () -> service.createTopicLike(1L, USERNAME));
        }
    }

    @DisplayName("createCommentLike method")
    @Nested
    class CreateCommentLikeTests {

        @DisplayName("Should create comment")
        @Test
        void shouldCreateComment() {
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(likeRepository.existsByCommentAndUser(any(Comment.class), any(User.class))).thenReturn(false);

            LikeDTO likeCreated = service.createCommentLike(1L, USERNAME);

            assertEquals(likeCreated.who(), USERNAME);
            assertEquals(likeCreated.likedObjectId(), 1L);
            assertEquals(likeCreated.likedObject(), STRING_COMMENT);
        }

        @DisplayName("Should throw CommentDoesntExistException when comment with given id doesn't exist")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentWithGivenIdDoesntExist() {
            assertThrows(CommentDoesntExistException.class, () -> service.createCommentLike(1L, USERNAME));
        }

        @DisplayName("Should throw LikeAlreadyExistException when like already exist")
        @Test
        void shouldThrowLikeExistException_WhenLikeAlreadyExist() {
            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(likeRepository.existsByCommentAndUser(any(Comment.class), any(User.class))).thenReturn(true);

            assertThrows(LikeAlreadyExistException.class, () -> service.createCommentLike(1L, USERNAME));
        }

        @DisplayName("Should TopicIsClosedException when topic of comment where like should be created in is closed")
        @Test
        void shouldTopicIsClosedException_WhenTopicOfCommentWhereLikeShouldBeCreatedInIsClosed() {
            topic.setClosed(true);

            when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

            assertThrows(TopicIsClosedException.class, () -> service.createCommentLike(1L, USERNAME));
        }
    }

    @DisplayName("getLikeById method")
    @Nested
    class GetLikeByIdTests {

        @DisplayName("Should get like")
        @Test
        void shouldGetLike() {
            when(likeRepository.findById(any(Long.class))).thenReturn(Optional.of(like));

            LikeDTO likeFound = service.getLikeById(1L);

            assertEquals(likeFound.who(), USERNAME);
        }

        @DisplayName("Should throw LikeDoesntExist when like with given id doesn't exist")
        @Test
        void shouldThrowLikeDoesntExistException_WhenLikeWithGivenIdDoesntExist() {
            assertThrows(LikeDoesntExistException.class, () -> service.getLikeById(1L));
        }
    }

    @DisplayName("getAllLikes method")
    @Nested
    class GetAllLikesTests {

        @DisplayName("Should return all likes")
        @Test
        void shouldReturnAllLikes() {
            when(likeRepository.findAll()).thenReturn(List.of(like, like));

            List<LikeDTO> likesFound = service.getAllLikes();

            assertEquals(likesFound.size(), 2);
            assertEquals(likesFound.get(0).who(), USERNAME);
        }
    }

    @DisplayName("getAllLikesByUsername method")
    @Nested
    class GetAllLikesByUsernameTests {

        @DisplayName("Should return all likes by username")
        @Test
        void shouldReturnAllLikesByUsername() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(likeRepository.findAllByUser(any(User.class))).thenReturn(List.of(like, like));

            List<LikeDTO> likesFound = service.getAllLikesByUsername(USERNAME);

            assertEquals(likesFound.size(), 2);
            assertEquals(likesFound.get(0).who(), USERNAME);
        }
    }

    @DisplayName("deleteLikeByCurrentUser method")
    @Nested
    class DeleteLikeByCurrentUserTests {

        @DisplayName("Should delete like")
        @Test
        void shouldDeleteLike() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            when(likeRepository.existsById(any(Long.class))).thenReturn(true);
            when(likeRepository.findByUserAndId(any(User.class), any(Long.class))).thenReturn(Optional.of(like));

            service.deleteLikeByAuthor(1L, USERNAME);

            verify(likeRepository, times(1)).delete(like);
        }

        @DisplayName("Should throw LikeDoesntExistException when like with given id doesn't exist")
        @Test
        void shouldThrowLikeDoesntExistException_WhenLikeWithGivenIdDoesntExist() {
            when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));
            assertThrows(LikeDoesntExistException.class, () -> service.deleteLikeByAuthor(1L, USERNAME));
        }
    }

    @DisplayName("deleteLikeByModerator method")
    @Nested
    class DeleteLikeByModeratorTests {

        @DisplayName("Should delete like")
        @Test
        void shouldDeleteLike() {
            when(likeRepository.findById(any(Long.class))).thenReturn(Optional.of(like));
            service.deleteLikeByModerator(1L);
            verify(likeRepository, times(1)).delete(like);
        }

        @DisplayName("Should throw LikeDoesntExistException when like with given id doesn't exist")
        @Test
        void shouldThrowLikeDoesntExistException_WhenLikeWithGivenIdDoesntExist() {
            assertThrows(LikeDoesntExistException.class, () -> service.deleteLikeByModerator(1L));
        }
    }
}