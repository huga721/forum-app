package huberts.spring.forumapp.like;

import huberts.spring.forumapp.like.dto.LikeDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    ResponseEntity<LikeDTO> getLikeById(@PathVariable Long id) {
        LikeDTO like = service.getLikeById(id);
        return ResponseEntity.ok(like);
    }

    @UserRole
    @GetMapping("/user")
    ResponseEntity<List<LikeDTO>> getAllLikesByCurrentUser(Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        List<LikeDTO> likes = service.getAllLikesByUsername(username);
        return ResponseEntity.ok(likes);
    }

    @UserRole
    @PostMapping("/topic/{id}")
    ResponseEntity<LikeDTO> saveNewTopicLike(@PathVariable Long id, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        LikeDTO likeCreated = service.createTopicLike(id, username);
        return ResponseEntity.ok(likeCreated);
    }

    @UserRole
    @PostMapping("/comment/{id}")
    ResponseEntity<LikeDTO> saveNewCommentLike(@PathVariable Long id, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        LikeDTO likeCreated = service.createCommentLike(id, username);
        return ResponseEntity.ok(likeCreated);
    }

    @UserRole
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteLikeByCurrentUser(@PathVariable Long id, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteLikeByCurrentUser(id, username);
        return ResponseEntity.noContent().build();
    }

    @ModeratorRole
    @DeleteMapping("/moderator/delete/{id}")
    ResponseEntity<Void> deleteLikeByModerator(@PathVariable Long id) {
        service.deleteLikeByModerator(id);
        return ResponseEntity.noContent().build();
    }
}