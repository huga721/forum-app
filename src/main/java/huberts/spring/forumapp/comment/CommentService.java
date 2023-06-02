package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.user.UserIsNotAuthorException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicRepository;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.utility.UtilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService implements CommentServiceApi {

    private static final String TOPIC_ID_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private static final String TOPIC_IS_CLOSED_EXCEPTION = "Topic with id \"%d\" is closed.";
    private static final String USER_IS_NOT_AUTHOR_EXCEPTION = "User is not author of comment with id \"%d\".";
    private static final String COMMENT_DOESNT_EXIST_EXCEPTION = "Comment with id \"%d\" doesn't exist.";
    private final static String USER_DOESNT_EXIST_EXCEPTION = "User with username \"%s\" doesn't exists.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UtilityService utilityService;

    @Override
    public CommentDTO createComment(Long topicId, CommentContentDTO commentContentDTO, String username) {
        log.info("Creating a comment for topic with id {}", topicId);
        Topic topicFound = findTopicById(topicId);
        User user = userRepository.findByUsername(username).get();

        validateTopicClosed(topicFound);
        utilityService.updateUserLastActivity(username);
        log.info("Comment created");
        return buildAndSaveComment(user, topicFound, commentContentDTO);
    }

    private CommentDTO buildAndSaveComment(User user, Topic topic, CommentContentDTO commentContentDTO) {
        Comment comment = CommentMapper.buildComment(user, topic, commentContentDTO.content());
        commentRepository.save(comment);
        return CommentMapper.buildCommentDTO(comment);
    }

    private void validateTopicClosed(Topic topic) {
        if (topic.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, topic.getId());
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
    }

    private Topic findTopicById(Long topicId) {
        log.info("Finding topic with id {}", topicId);
        return topicRepository.findById(topicId).orElseThrow(() -> {
            String errorMessage = String.format(TOPIC_ID_DOESNT_EXIST_EXCEPTION, topicId);
            log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
            return new TopicDoesntExistException(errorMessage);
        });
    }

    @Override
    public CommentDTO getCommentById(Long commentId) {
        log.info("Getting comment with id {}", commentId);
        Comment result = findCommentById(commentId);
        return CommentMapper.buildCommentDTO(result);
    }

    private Comment findCommentById(Long commentId) {
        log.info("Finding comment with id {}", commentId);
        return commentRepository.findById(commentId).orElseThrow(() -> {
            String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, commentId);
            log.error(EXCEPTION_OCCURRED, new CommentDoesntExistException(errorMessage));
            return new CommentDoesntExistException(errorMessage);
        });
    }

    @Override
    public List<CommentDTO> getAllComments() {
        log.info("Getting all comments");
        return CommentMapper.mapCommentListToCommentDTOList(commentRepository.findAll());
    }

    @Override
    public List<CommentDTO> getAllCommentsByTopicId(Long topicId) {
        log.info("Getting all comments by topic with id {}", topicId);
        Topic topicFound = findTopicById(topicId);
        return CommentMapper.mapCommentListToCommentDTOList(commentRepository.findAllByTopic(topicFound));
    }

    @Override
    public List<CommentDTO> getAllCommentsByUsername(String username) {
        log.info("Getting all comments by username {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            String errorMessage = String.format(USER_DOESNT_EXIST_EXCEPTION, username);
            log.error(EXCEPTION_OCCURRED, new UserDoesntExistException(errorMessage));
            return new UserDoesntExistException(errorMessage);
        });
        return CommentMapper.mapCommentListToCommentDTOList(commentRepository.findAllByUser(user));
    }

    @Override
    public CommentDTO updateCommentByAuthor(Long commentId, CommentContentDTO commentContentDTO, String username) {
        log.info("Updating content of comment with id {} by username {}", commentId, username);
        User author = userRepository.findByUsername(username).get();
        Comment commentFound = findCommentByUserAndId(author, commentId);

        validateTopicClosed(commentFound.getTopic());
        utilityService.updateUserLastActivity(username);
        log.info("Comment updated");
        return updateContentAndBuildComment(commentFound, commentContentDTO);
    }

    private CommentDTO updateContentAndBuildComment(Comment comment, CommentContentDTO commentContentDTO) {
        comment.setContent(commentContentDTO.content());
        return CommentMapper.buildCommentDTO(comment);
    }

    private Comment findCommentByUserAndId(User user, Long commentId) {
        log.info("Finding comment with id {} created by {}", commentId, user.getUsername());
        if (commentRepository.existsById(commentId)) {
            return commentRepository.findByUserAndId(user, commentId).orElseThrow(() -> {
                String errorMessage = String.format(USER_IS_NOT_AUTHOR_EXCEPTION, commentId);
                log.error(EXCEPTION_OCCURRED, new UserIsNotAuthorException(errorMessage));
                return new UserIsNotAuthorException(errorMessage);
            });
        }
        String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, commentId);
        log.error(EXCEPTION_OCCURRED, new CommentDoesntExistException(errorMessage));
        throw new CommentDoesntExistException(errorMessage);
    }

    @Override
    public CommentDTO updateCommentByModerator(Long commentId, CommentContentDTO commentContentDTO, String username) {
        log.info("Updating content of comment with id {} by moderator or admin", commentId);
        Comment commentFound = findCommentById(commentId);

        validateTopicClosed(commentFound.getTopic());
        utilityService.updateUserLastActivity(username);
        log.info("Comment updated");
        return updateContentAndBuildComment(commentFound, commentContentDTO);
    }

    @Override
    public void deleteCommentByAuthor(Long commentId, String username) {
        log.info("Deleting comment with id {} by username {}", commentId, username);
        User author = userRepository.findByUsername(username).get();
        Comment commentFound = findCommentByUserAndId(author, commentId);

        validateTopicClosed(commentFound.getTopic());
        utilityService.updateUserLastActivity(username);
        commentRepository.delete(commentFound);
        log.info("Comment deleted");
    }

    @Override
    public void deleteCommentByModerator(Long commentId, String username) {
        log.info("Deleting comment with id {} by moderator or admin", commentId);
        Comment foundComment = findCommentById(commentId);
        validateTopicClosed(foundComment.getTopic());

        utilityService.updateUserLastActivity(username);
        commentRepository.delete(foundComment);
        log.info("Comment deleted");
    }
}