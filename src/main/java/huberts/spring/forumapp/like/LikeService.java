package huberts.spring.forumapp.like;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.comment.CommentRepository;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.like.LikeAlreadyExistException;
import huberts.spring.forumapp.exception.like.LikeDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.exception.user.UserIsNotAuthorException;
import huberts.spring.forumapp.like.dto.LikeDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicRepository;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService implements LikeServiceApi {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;

    private static final String TOPIC_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private static final String TOPIC_LIKE_EXIST_EXCEPTION = "Topic is already liked.";
    private static final String COMMENT_LIKE_EXIST_EXCEPTION = "Comment is already liked.";
    private static final String LIKE_DOESNT_EXIST_EXCEPTION = "Like with id \"%d\" doesn't exist.";
    private static final String COMMENT_DOESNT_EXIST_EXCEPTION = "Comment with id \"%d\" doesn't exist.";
    private static final String LIKE_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION = "There is no like given by current user with id \"%d\".";
    private static final String TOPIC_IS_CLOSED_EXCEPTION = "Topic which like is created is closed.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";
    private static final String USER_IS_NOT_AUTHOR_EXCEPTION = "User is not author of like with id \"%d\".";


    @Override
    public LikeDTO createTopicLike(Long topicId, String username) {
        log.info("Creating a like for topic with id {}", topicId);
        Topic topicFound = topicRepository.findById(topicId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_DOESNT_EXIST_EXCEPTION, topicId);
                    log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
                    throw new TopicDoesntExistException(errorMessage);
                });
        User userCreated = userRepository.findByUsername(username).get();

        if (topicFound.isClosed()) {
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION));
            throw new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION);
        }
        if (likeRepository.existsByTopicAndUser(topicFound, userCreated)) {
            log.error(EXCEPTION_OCCURRED, new LikeAlreadyExistException(TOPIC_LIKE_EXIST_EXCEPTION));
            throw new LikeAlreadyExistException(TOPIC_LIKE_EXIST_EXCEPTION);
        }

        Like likeCreated = LikeMapper.buildTopicLike(userCreated, topicFound);
        likeRepository.save(likeCreated);
        log.info("Like created");
        return LikeMapper.buildLikeDTO(likeCreated);
    }

    @Override
    public LikeDTO createCommentLike(Long commentId, String username) {
        log.info("Creating a like for comment with id {}", commentId);
        Comment commentToLike = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, commentId);
                    log.error(EXCEPTION_OCCURRED, new CommentDoesntExistException(errorMessage));
                    return new CommentDoesntExistException(errorMessage);
                });
        User userCreated = userRepository.findByUsername(username).get();

        if (isTopicClosed(commentToLike)) {
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION));
            throw new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION);
        }
        if (likeRepository.existsByCommentAndUser(commentToLike, userCreated)) {
            log.error(EXCEPTION_OCCURRED, new LikeAlreadyExistException(COMMENT_LIKE_EXIST_EXCEPTION));
            throw new LikeAlreadyExistException(COMMENT_LIKE_EXIST_EXCEPTION);
        }

        Like likeCreated = LikeMapper.buildCommentLike(userCreated, commentToLike);
        likeRepository.save(likeCreated);
        log.info("Like created");
        return LikeMapper.buildLikeDTO(likeCreated);
    }

    private boolean isTopicClosed (Comment comment) {
        return comment.getTopic().isClosed();
    }

    private boolean isTopicClosed (Like like) {
        if (like.getTopic() != null) {
            return like.getTopic().isClosed();
        }
        return like.getComment().getTopic().isClosed();
    }

    @Override
    public LikeDTO getLikeById(Long likeId) {
        log.info("Getting like with id {}", likeId);
        Like likeFound = findById(likeId);
        return LikeMapper.buildLikeDTO(likeFound);
    }

    private Like findById(Long likeId) {
        log.info("Finding like with id {}", likeId);
        return likeRepository.findById(likeId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(LIKE_DOESNT_EXIST_EXCEPTION, likeId);
                    log.error(EXCEPTION_OCCURRED, new LikeDoesntExistException(errorMessage));
                    throw new LikeDoesntExistException(String.format(LIKE_DOESNT_EXIST_EXCEPTION, likeId));
                });
    }

    @Override
    public List<LikeDTO> getAllLikes() {
        log.info("Getting all likes");
        return LikeMapper.mapLikesToLikeDTO(likeRepository.findAll());
    }

    @Override
    public List<LikeDTO> getAllLikesByUsername(String username) {
        log.info("Getting all likes by username {}", username);
        User user = userRepository.findByUsername(username).get();
        return LikeMapper.mapLikesToLikeDTO(likeRepository.findAllByUser(user));
    }

        @Override
        public void deleteLikeByAuthor(Long likeId, String username) {
            log.info("Deleting like with id {} by username {}", likeId, username);
            User userCreated = userRepository.findByUsername(username).get();
            Like likeFound = findLikeByUserAndId(userCreated, likeId);
            if (isTopicClosed(likeFound)) {
                log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION));
                throw new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION);
            }
            likeRepository.delete(likeFound);
            log.info("Like deleted");
        }

        private Like findLikeByUserAndId(User user, Long likeId) {
            log.info("Finding like with id {} created by user {}", likeId, user.getUsername());
            if (likeRepository.existsById(likeId)) {
                return likeRepository.findByUserAndId(user, likeId).orElseThrow(() -> {
                    String errorMessage = String.format(USER_IS_NOT_AUTHOR_EXCEPTION, likeId);
                    log.error(EXCEPTION_OCCURRED, new UserIsNotAuthorException(errorMessage));
                    throw new UserIsNotAuthorException(errorMessage);
                });
            }
            String errorMessage = String.format(LIKE_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION, likeId);
            log.error(EXCEPTION_OCCURRED, new LikeDoesntExistException(errorMessage));
            throw new LikeDoesntExistException(errorMessage);
        }

    @Override
    public void deleteLikeByModerator(Long id) {
        log.info("Deleting like with id {} by moderator or admin", id);
        Like likeFound = findById(id);
        if (isTopicClosed(likeFound)) {
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION));
            throw new TopicIsClosedException(TOPIC_IS_CLOSED_EXCEPTION);
        }
        likeRepository.delete(likeFound);
        log.info("Like deleted");
    }
}