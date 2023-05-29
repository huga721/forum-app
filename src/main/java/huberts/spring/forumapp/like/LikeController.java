package huberts.spring.forumapp.like;

import huberts.spring.forumapp.like.dto.LikeDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService service;

    @GetMapping()
    ResponseEntity<List<LikeDTO>> getAllLikes() {
        List<LikeDTO> likes = service.getAllLikes();
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/{likeId}")
    ResponseEntity<LikeDTO> getLikeById(@PathVariable Long likeId) {
        LikeDTO like = service.getLikeById(likeId);
        return ResponseEntity.ok(like);
    }

    @UserRole
    @GetMapping("/user")
    ResponseEntity<List<LikeDTO>> getAllLikesOfAuthenticatedUser(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        List<LikeDTO> likes = service.getAllLikesByUsername(username);
        return ResponseEntity.ok(likes);
    }

    @UserRole
    @PostMapping("/topic/{topicId}")
    ResponseEntity<LikeDTO> createTopicLike(@PathVariable Long topicId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        LikeDTO likeCreated = service.createTopicLike(topicId, username);
        return ResponseEntity.created(URI.create("/likes")).body(likeCreated);
    }

    @UserRole
    @PostMapping("/comment/{commentId}")
    ResponseEntity<LikeDTO> createCommentLike(@PathVariable Long commentId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        LikeDTO likeCreated = service.createCommentLike(commentId, username);
        return ResponseEntity.created(URI.create("/likes")).body(likeCreated);
    }

    @UserRole
    @DeleteMapping("/delete/{commentId}")
    ResponseEntity<Void> deleteLikeByAuthor(@PathVariable Long commentId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteLikeByAuthor(commentId, username);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @DeleteMapping("/moderator/delete/{commentId}")
    ResponseEntity<Void> deleteLikeByModerator(@PathVariable Long commentId) {
        service.deleteLikeByModerator(commentId);
        return ResponseEntity.noContent().build();
    }
}