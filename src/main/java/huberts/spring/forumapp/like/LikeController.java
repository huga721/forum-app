package huberts.spring.forumapp.like;

import huberts.spring.forumapp.like.dto.LikeDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/likes")
public class LikeController {

    private final LikeService service;

    @Operation(summary = "Get all likes")
    @GetMapping()
    List<LikeDTO> getAllLikes() {
        List<LikeDTO> likes = service.getAllLikes();
        return likes;
    }

    @Operation(summary = "Get like by id")
    @GetMapping("/{likeId}")
    LikeDTO getLikeById(@PathVariable Long likeId) {
        LikeDTO like = service.getLikeById(likeId);
        return like;
    }

    @UserRole
    @Operation(summary = "[USER] Get all likes")
    @GetMapping("/user")
    List<LikeDTO> getAllLikesByUsername(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        List<LikeDTO> likes = service.getAllLikesByUsername(username);
        return likes;
    }

    @UserRole
    @Operation(summary = "[USER] Create like of topic")
    @PostMapping("/topic/{topicId}")
    ResponseEntity<LikeDTO> createTopicLike(@PathVariable Long topicId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        LikeDTO likeCreated = service.createTopicLike(topicId, username);
        return ResponseEntity.created(URI.create("/likes")).body(likeCreated);
    }

    @UserRole
    @Operation(summary = "[USER] Create like of comment")
    @PostMapping("/comment/{commentId}")
    ResponseEntity<LikeDTO> createCommentLike(@PathVariable Long commentId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        LikeDTO likeCreated = service.createCommentLike(commentId, username);
        return ResponseEntity.created(URI.create("/likes")).body(likeCreated);
    }

    @UserRole
    @Operation(summary = "[USER] Delete like of authenticated user by comment id")
    @DeleteMapping("/delete/{commentId}")
    ResponseEntity<Void> deleteLikeByAuthor(@PathVariable Long commentId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteLikeByAuthor(commentId, username);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @Operation(summary = "[USER] Delete like by comment id")
    @DeleteMapping("/moderator/delete/{commentId}")
    ResponseEntity<Void> deleteLikeByModerator(@PathVariable Long commentId) {
        service.deleteLikeByModerator(commentId);
        return ResponseEntity.noContent().build();
    }
}