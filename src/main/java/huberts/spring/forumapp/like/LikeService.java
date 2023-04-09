package huberts.spring.forumapp.like;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.comment.CommentRepository;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.like.LikeExistException;
import huberts.spring.forumapp.exception.topic.TopicAlreadyExistException;
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
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    @Override
    public LikeDTO createTopicLike(Long id, String username) {
        log.info("Creating a like for topic with id {}", id);

        Topic topicToLike = topicRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(TOPIC_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new TopicAlreadyExistException(errorMessage));
                    return new TopicAlreadyExistException(errorMessage);
                });
        User userCreated = userRepository.findByUsername(username).get();

        if (likeRepository.existsByTopicAndUser(topicToLike, userCreated)) {
            log.error(EXCEPTION_OCCURRED, new LikeExistException(TOPIC_LIKE_EXIST_EXCEPTION));
            throw new LikeExistException(TOPIC_LIKE_EXIST_EXCEPTION);
        }

        Like likeCreated = LikeMapper.buildTopicLike(userCreated, topicToLike);
        Like likeSaved = likeRepository.save(likeCreated);

        log.info("Like created");
        return LikeMapper.buildLikeObjectDTO(likeSaved);
    }

    @Override
    public LikeDTO createCommentLike(Long id, String username) {
        log.info("Creating a like for comment with id {}", id);

        Comment commentToLike = commentRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new CommentDoesntExistException(errorMessage));
                    return new CommentDoesntExistException(errorMessage);
                });
        User userCreated = userRepository.findByUsername(username).get();

        if (likeRepository.existsByCommentAndUser(commentToLike, userCreated)) {
            log.error(EXCEPTION_OCCURRED, new LikeExistException(COMMENT_LIKE_EXIST_EXCEPTION));
            throw new LikeExistException(COMMENT_LIKE_EXIST_EXCEPTION);
        }

        Like likeCreated = LikeMapper.buildCommentLike(userCreated, commentToLike);
        Like likeSaved = likeRepository.save(likeCreated);

        log.info("Like created");
        return LikeMapper.buildLikeObjectDTO(likeSaved);
    }

    @Override
    public LikeDTO getLikeById(Long id) {
        log.info("Getting like with id {}", id);
        Like likeFound = findById(id);
        return LikeMapper.buildLikeObjectDTO(likeFound);
    }

    private Like findById(Long id) {
        log.info("Finding like with id {}", id);
        return likeRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(LIKE_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new LikeExistException(errorMessage));
                    return new LikeExistException(String.format(LIKE_DOESNT_EXIST_EXCEPTION, id));
                });
    }

    @Override
    public List<LikeDTO> getAllLikes() {
        log.info("Getting all likes");
        return LikeMapper.mapLikesToLikeObjectDTO(likeRepository.findAll());
    }

    @Override
    public List<LikeDTO> getAllLikesByUsername(String username) {
        log.info("Getting all likes by username {}", username);
        User user = userRepository.findByUsername(username).get();
        return LikeMapper.mapLikesToLikeObjectDTO(likeRepository.findAllByUser(user));
    }

    @Override
    public void deleteLikeByCurrentUser(Long id, String username) {
        log.info("Deleting like with id {} by username {}", id, username);
        User userCreated = userRepository.findByUsername(username).get();
        Like likeFound = likeRepository.findByUserAndId(userCreated, id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(LIKE_DOESNT_EXIST_WITH_GIVEN_USER_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new LikeExistException(errorMessage));
                    return new LikeExistException(errorMessage);
                });
        likeRepository.delete(likeFound);
        log.info("Like deleted");
    }

    @Override
    public void deleteLikeByModerator(Long id) {
        log.info("Deleting like with id {} by moderator or admin", id);
        Like likeFound = findById(id);
        likeRepository.delete(likeFound);
        log.info("Like deleted");
    }
}