package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;

import java.util.List;

public interface CommentServiceApi {
    CommentDTO createComment(Long commentId, CommentContentDTO commentDTO, String username);

    CommentDTO getCommentById(Long commentId);
    List<CommentDTO> getAllComments();
    List<CommentDTO> getAllCommentsByTopicId(Long commentId);
    List<CommentDTO> getAllCommentsByUsername(String username);

    CommentDTO updateCommentByAuthor(Long commentId, CommentContentDTO commentContentDTO, String username);
    CommentDTO updateCommentByModerator(Long commentId, CommentContentDTO commentContentDTO, String username);

    void deleteCommentByAuthor(Long commentId, String username);
    void deleteCommentByModerator(Long commentId, String username);
}