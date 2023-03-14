package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;

import java.util.List;

public interface CommentServiceApi {
    CommentDTO createComment(Long id, CommentContentDTO commentDTO, String username);

    CommentDTO getCommentById(Long id);
    List<CommentDTO> getAllComments();
    List<CommentDTO> getAllCommentsByTopicId(Long id);
    List<CommentDTO> getAllCommentsByUsername(String username);

    CommentDTO updateCommentByUser(Long id, CommentContentDTO commentContentDTO, String username);
    CommentDTO updateCommentByModerator(Long id, CommentContentDTO commentContentDTO);

    void deleteCommentByCurrentUser(Long id, String username);
    void deleteCommentByModerator(Long id);
}