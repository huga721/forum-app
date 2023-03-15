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

    @GetMapping("/{id}")
    ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = service.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/topic/{id}")
    ResponseEntity<List<CommentDTO>> getAllCommentsOfTopic(@PathVariable Long id) {
        List<CommentDTO> commentsOfTopic = service.getAllCommentsByTopicId(id);
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
    @PostMapping("/topic/{id}")
    ResponseEntity<CommentDTO> saveNewComment(@PathVariable Long id, @RequestBody @Valid CommentContentDTO createCommentDTO,
                                                Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        CommentDTO commentCreated = service.createComment(id, createCommentDTO, username);
        return ResponseEntity.created(URI.create("/comments")).body(commentCreated);
    }

    @UserRole
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteCommentByCurrentUser(@PathVariable Long id, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteCommentByAuthor(id, username);
        return ResponseEntity.noContent().build();
    }

    @UserRole
    @PatchMapping("/edit/{id}")
    ResponseEntity<CommentDTO> editCommentByCurrentUser(@PathVariable Long id, @RequestBody @Valid CommentContentDTO contentDTO,
                                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        CommentDTO commentEdited = service.updateCommentByAuthor(id, contentDTO, username);
        return ResponseEntity.ok(commentEdited);
    }

    @ModeratorRole
    @DeleteMapping("/moderator/delete/{id}")
    ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        service.deleteCommentByModerator(id);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @PatchMapping("/moderator/edit/{id}")
    ResponseEntity<CommentDTO> editCommentByModerator(@PathVariable Long id, @RequestBody @Valid CommentContentDTO contentDTO) {
        CommentDTO commentEdited = service.updateCommentByModerator(id, contentDTO);
        return ResponseEntity.ok(commentEdited);
    }
}