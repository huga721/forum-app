package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.exception.comment.CommentExistException;
import huberts.spring.forumapp.exception.topic.TopicExistException;
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

    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final String TOPIC_ID_DOESNT_EXIST_EXCEPTION = "Topic with id \"%d\" doesn't exist.";
    private final String COMMENT_DOESNT_EXIST_WITH_CURRENT_USER_EXCEPTION = "There is no comment created by current user with id \"%d\".";
    private final String COMMENT_DOESNT_EXIST_EXCEPTION = "Comment with id \"%d\" doesn't exist.";

    @Override
    public CommentDTO createComment(Long id, CommentContentDTO commentDTO, String username) {
        log.info("Creating a comment for topic with id {}", id);
        Topic topicFound = findTopicById(id);
        User user = userRepository.findByUsername(username).get();

        Comment comment = CommentMapper.buildNewComment(user, topicFound, commentDTO.getContent());
        Comment savedComment = commentRepository.save(comment);

        log.info("Comment created");
        return CommentMapper.buildCommentDTO(savedComment);
    }

    private Topic findTopicById(Long id) {
        log.info("Finding topic with id {}", id);
        return topicRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format(TOPIC_ID_DOESNT_EXIST_EXCEPTION, id));
                    return new TopicExistException(String.format(TOPIC_ID_DOESNT_EXIST_EXCEPTION, id));
                });
    }

    @Override
    public void deleteCommentByCurrentUser(Long id, String username) {
        log.info("Deleting comment with id {} by username {}", id, username);
        User user = userRepository.findByUsername(username).get();
        Comment foundComment = findCommentByUserAndId(user, id);
        commentRepository.delete(foundComment);
        log.info("Comment deleted");
    }

    private Comment findCommentByUserAndId(User user, Long id) {
        log.info("Finding comment with id {} and username {}", id, user.getUsername());
        return commentRepository.findByUserAndId(user, id)
                .orElseThrow(() -> {
                    log.error(String.format(COMMENT_DOESNT_EXIST_WITH_CURRENT_USER_EXCEPTION, id));
                    return new CommentExistException(String.format(COMMENT_DOESNT_EXIST_WITH_CURRENT_USER_EXCEPTION, id));
                });
    }

    @Override
    public void deleteCommentByModerator(Long id) {
        log.info("Deleting comment with id {} by moderator or admin", id);
        Comment foundComment = findCommentById(id);
        commentRepository.delete(foundComment);
        log.info("Comment deleted");
    }

    private Comment findCommentById(Long id) {
        log.info("Finding comment with id {}", id);
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(String.format(COMMENT_DOESNT_EXIST_EXCEPTION, id));
                    return new CommentExistException(String.format(COMMENT_DOESNT_EXIST_EXCEPTION, id));
                });
    }

    @Override
    public CommentDTO updateCommentByUser(Long id, CommentContentDTO commentContentDTO, String username) {
        log.info("Updating content of comment with id {} by username {}", id, username);
        User user = userRepository.findByUsername(username).get();
        Comment foundComment = findCommentByUserAndId(user, id);
        foundComment.setContent(commentContentDTO.getContent());
        log.info("Comment updated");
        return CommentMapper.buildCommentDTO(foundComment);
    }

    @Override
    public CommentDTO updateCommentByModerator(Long id, CommentContentDTO commentContentDTO) {
        log.info("Updating content of comment with id {} by moderator or admin", id);
        Comment foundComment = findCommentById(id);
        foundComment.setContent(commentContentDTO.getContent());
        log.info("Comment updated");
        return CommentMapper.buildCommentDTO(foundComment);
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
        User user = userRepository.findByUsername(username).get();
        return CommentMapper.mapToCommentDTO(commentRepository.findAllByUser(user));
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        log.info("Getting comment with id {}", id);
        return CommentMapper.buildCommentDTO(findCommentById(id));
    }
}