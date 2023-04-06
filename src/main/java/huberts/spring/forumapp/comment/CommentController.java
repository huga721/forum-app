package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService service;

    @GetMapping()
    ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = service.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        CommentDTO comment = service.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/topic/{commentId}")
    ResponseEntity<List<CommentDTO>> getAllCommentsOfTopic(@PathVariable Long commentId) {
        List<CommentDTO> commentsOfTopic = service.getAllCommentsByTopicId(commentId);
        return ResponseEntity.ok(commentsOfTopic);
    }

    @GetMapping("/user/{username}")
    ResponseEntity<List<CommentDTO>> getAllCommentsByUsername(@PathVariable String username) {
        List<CommentDTO> commentsOfUser = service.getAllCommentsByUsername(username);
        return ResponseEntity.ok(commentsOfUser);
    }

    @UserRole
    @GetMapping("/user")
    ResponseEntity<List<CommentDTO>> getAllCurrentUserComments(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        List<CommentDTO> commentsOfCurrentUser = service.getAllCommentsByUsername(username);
        return ResponseEntity.ok(commentsOfCurrentUser);
    }

    @UserRole
    @PostMapping("/topic/{commentId}")
    ResponseEntity<CommentDTO> saveNewComment(@PathVariable Long commentId, @RequestBody @Valid CommentContentDTO createCommentDTO,
                                                Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        CommentDTO commentCreated = service.createComment(commentId, createCommentDTO, username);
        return ResponseEntity.created(URI.create("/comments")).body(commentCreated);
    }

    @UserRole
    @DeleteMapping("/delete/{commentId}")
    ResponseEntity<Void> deleteCommentByCurrentUser(@PathVariable Long commentId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteCommentByAuthor(commentId, username);
        return ResponseEntity.noContent().build();
    }

    @UserRole
    @PatchMapping("/edit/{commentId}")
    ResponseEntity<CommentDTO> editCommentByCurrentUser(@PathVariable Long commentId, @RequestBody @Valid CommentContentDTO contentDTO,
                                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        CommentDTO commentEdited = service.updateCommentByAuthor(commentId, contentDTO, username);
        return ResponseEntity.ok(commentEdited);
    }

    @ModeratorRole
    @DeleteMapping("/moderator/delete/{commentId}")
    ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        service.deleteCommentByModerator(commentId);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @PatchMapping("/moderator/edit/{commentId}")
    ResponseEntity<CommentDTO> editCommentByModerator(@PathVariable Long commentId, @RequestBody @Valid CommentContentDTO contentDTO) {
        CommentDTO commentEdited = service.updateCommentByModerator(commentId, contentDTO);
        return ResponseEntity.ok(commentEdited);
    }
}