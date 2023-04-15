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

    @Override
    public CommentDTO createComment(Long id, CommentContentDTO commentDTO, String username) {
        log.info("Creating a comment for topic with id {}", id);
        Topic topicFound = findTopicById(id);
        User user = userRepository.findByUsername(username).get();
        if (topicFound.isClosed()) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
        Comment comment = CommentMapper.buildNewComment(user, topicFound, commentDTO.getContent());
        commentRepository.save(comment);
        log.info("Comment created");
        return CommentMapper.buildCommentDTO(comment);
    }

    private Topic findTopicById(Long id) {
        log.info("Finding topic with id {}", id);
        return topicRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format(TOPIC_ID_DOESNT_EXIST_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicDoesntExistException(errorMessage));
            return new TopicDoesntExistException(errorMessage);
        });
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        log.info("Getting comment with id {}", id);
        Comment result = findCommentById(id);
        return CommentMapper.buildCommentDTO(result);
    }

    private Comment findCommentById(Long id) {
        log.info("Finding comment with id {}", id);
        return commentRepository.findById(id).orElseThrow(() -> {
            String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new CommentDoesntExistException(errorMessage));
            return new CommentDoesntExistException(errorMessage);
        });
    }

    @Override
    public List<CommentDTO> getAllComments() {
        log.info("Getting all comments");
        return CommentMapper.mapToCommentDTO(commentRepository.findAll());
    }

    @Override
    public List<CommentDTO> getAllCommentsByTopicId(Long id) {
        log.info("Getting all comments by topic with id {}", id);
        Topic topicFound = findTopicById(id);
        return CommentMapper.mapToCommentDTO(commentRepository.findAllByTopic(topicFound));
    }

    @Override
    public List<CommentDTO> getAllCommentsByUsername(String username) {
        log.info("Getting all comments by username {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            String errorMessage = String.format(USER_DOESNT_EXIST_EXCEPTION, username);
            log.error(EXCEPTION_OCCURRED, new UserDoesntExistException(errorMessage));
            return new UserDoesntExistException(errorMessage);
        });
        return CommentMapper.mapToCommentDTO(commentRepository.findAllByUser(user));
    }

    @Override
    public CommentDTO updateCommentByAuthor(Long id, CommentContentDTO commentContentDTO, String username) {
        log.info("Updating content of comment with id {} by username {}", id, username);
        User user = userRepository.findByUsername(username).get();
        Comment foundComment = findCommentByUserAndId(user, id);
        return updateContentAndGetDTO(id, commentContentDTO, foundComment);
    }

    private boolean isTopicClosed (Comment comment) {
        return comment.getTopic().isClosed();
    }

    private Comment findCommentByUserAndId(User user, Long id) {
        log.info("Finding comment with id {} created by {}", id, user.getUsername());
        if (commentRepository.existsById(id)) {
            return commentRepository.findByUserAndId(user, id).orElseThrow(() -> {
                String errorMessage = String.format(USER_IS_NOT_AUTHOR_EXCEPTION, id);
                log.error(EXCEPTION_OCCURRED, new UserIsNotAuthorException(errorMessage));
                throw new UserIsNotAuthorException(errorMessage);
            });
        }
        String errorMessage = String.format(COMMENT_DOESNT_EXIST_EXCEPTION, id);
        log.error(EXCEPTION_OCCURRED, new CommentDoesntExistException(errorMessage));
        throw new CommentDoesntExistException(errorMessage);
    }

    @Override
    public CommentDTO updateCommentByModerator(Long id, CommentContentDTO commentContentDTO) {
        log.info("Updating content of comment with id {} by moderator or admin", id);
        Comment foundComment = findCommentById(id);
        return updateContentAndGetDTO(id, commentContentDTO, foundComment);
    }

    private CommentDTO updateContentAndGetDTO(Long id, CommentContentDTO commentContentDTO, Comment foundComment) {
        if (isTopicClosed(foundComment)) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
        foundComment.setContent(commentContentDTO.getContent());
        log.info("Comment updated");
        return CommentMapper.buildCommentDTO(foundComment);
    }

    @Override
    public void deleteCommentByAuthor(Long id, String username) {
        log.info("Deleting comment with id {} by username {}", id, username);
        User user = userRepository.findByUsername(username).get();
        Comment foundComment = findCommentByUserAndId(user, id);
        if (isTopicClosed(foundComment)) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
        commentRepository.delete(foundComment);
        log.info("Comment deleted");
    }

    @Override
    public void deleteCommentByModerator(Long id) {
        log.info("Deleting comment with id {} by moderator or admin", id);
        Comment foundComment = findCommentById(id);
        if (isTopicClosed(foundComment)) {
            String errorMessage = String.format(TOPIC_IS_CLOSED_EXCEPTION, id);
            log.error(EXCEPTION_OCCURRED, new TopicIsClosedException(errorMessage));
            throw new TopicIsClosedException(errorMessage);
        }
        commentRepository.delete(foundComment);
        log.info("Comment deleted");
    }
}