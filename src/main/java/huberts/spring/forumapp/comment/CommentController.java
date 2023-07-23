package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService service;

    @Operation(summary = "Get all comments")
    @GetMapping()
    List<CommentDTO> getAllComments() {
        List<CommentDTO> comments = service.getAllComments();
        return comments;
    }

    @Operation(summary = "Get comment by id")
    @GetMapping("/{commentId}")
    CommentDTO getCommentById(@PathVariable Long commentId) {
        CommentDTO comment = service.getCommentById(commentId);
        return comment;
    }

    @Operation(summary = "Get all comments by topic id")
    @GetMapping("/topic/{commentId}")
    List<CommentDTO> getAllCommentsByTopicId(@PathVariable Long commentId) {
        List<CommentDTO> commentsOfTopic = service.getAllCommentsByTopicId(commentId);
        return commentsOfTopic;
    }

    @Operation(summary = "Get all comments by username")
    @GetMapping("/user/{username}")
    List<CommentDTO> getAllCommentsByUsername(@PathVariable String username) {
        List<CommentDTO> commentsOfUser = service.getAllCommentsByUsername(username);
        return commentsOfUser;
    }

    @UserRole
    @Operation(summary = "[USER] Get all comments of authenticated user")
    @GetMapping("/user")
    List<CommentDTO> getAllCommentsByUsername(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        List<CommentDTO> commentsOfCurrentUser = service.getAllCommentsByUsername(username);
        return commentsOfCurrentUser;
    }

    @UserRole
    @Operation(summary = "[USER] Create comment by topic id")
    @PostMapping("/topic/{topicId}")
    ResponseEntity<CommentDTO> createComment(@PathVariable Long topicId, @RequestBody @Valid CommentContentDTO createCommentDTO,
                                              Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        CommentDTO commentCreated = service.createComment(topicId, createCommentDTO, username);
        return ResponseEntity.created(URI.create("/comments")).body(commentCreated);
    }

    @UserRole
    @Operation(summary = "[USER] Delete comment of authenticated user by comment id")
    @DeleteMapping("/delete/{commentId}")
    ResponseEntity<Void> deleteCommentByAuthor(@PathVariable Long commentId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteCommentByAuthor(commentId, username);
        return ResponseEntity.noContent().build();
    }

    @UserRole
    @Operation(summary = "[USER] Update comment of authenticated user by comment id")
    @PatchMapping("/edit/{commentId}")
    CommentDTO updateCommentByAuthor(@PathVariable Long commentId, @RequestBody @Valid CommentContentDTO contentDTO,
                                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        CommentDTO commentEdited = service.updateCommentByAuthor(commentId, contentDTO, username);
        return commentEdited;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Delete comment by comment id")
    @DeleteMapping("/moderator/delete/{commentId}")
    ResponseEntity<Void> deleteCommentByModerator(@PathVariable Long commentId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteCommentByModerator(commentId, username);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Update comment by comment id")
    @PatchMapping("/moderator/edit/{commentId}")
    CommentDTO updateCommentByModerator(@PathVariable Long commentId, @RequestBody @Valid CommentContentDTO contentDTO,
                                        Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        CommentDTO commentEdited = service.updateCommentByModerator(commentId, contentDTO, username);
        return commentEdited;
    }
}