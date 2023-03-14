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
    ResponseEntity<List<CommentDTO>> allComments() {
        return ResponseEntity.ok(service.getAllComments());
    }

    @GetMapping("/{id}")
    ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCommentById(id));
    }

    @GetMapping("/topic/{id}")
    ResponseEntity<List<CommentDTO>> getAllCommentsOfTopic(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAllCommentsByTopicId(id));
    }

    @GetMapping("/user/{username}")
    ResponseEntity<List<CommentDTO>> getAllCommentsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(service.getAllCommentsByUsername(username));
    }

    @UserRole
    @GetMapping("/user")
    ResponseEntity<List<CommentDTO>> getAllCurrentUserComments(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        return ResponseEntity.ok(service.getAllCommentsByUsername(username));
    }

    @UserRole
    @PostMapping("/topic/{id}")
    ResponseEntity<CommentDTO> saveNewComment(@PathVariable Long id, @RequestBody @Valid CommentContentDTO createCommentDTO,
                                                Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        return ResponseEntity.created(URI.create("/comments")).body(service.createComment(id, createCommentDTO, username));
    }

    @UserRole
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteCommentByCurrentUser(@PathVariable Long id, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteCommentByCurrentUser(id, username);
        return ResponseEntity.noContent().build();
    }

    @UserRole
    @PatchMapping("/edit/{id}")
    ResponseEntity<CommentDTO> editCommentByCurrentUser(@PathVariable Long id, @RequestBody @Valid CommentContentDTO contentDTO,
                                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        return ResponseEntity.ok(service.updateCommentByUser(id, contentDTO, username));
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
        return ResponseEntity.ok(service.updateCommentByModerator(id, contentDTO));
    }
}