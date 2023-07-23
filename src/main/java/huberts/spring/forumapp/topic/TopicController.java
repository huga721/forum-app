package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.dto.UpdateTopicCategoryDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import huberts.spring.forumapp.topic.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService service;

    @Operation(summary = "Get all topics")
    @GetMapping()
    List<TopicDTO> getAllTopics() {
        List<TopicDTO> topics = service.getAllTopics();
        return topics;
    }

    @Operation(summary = "Get topic by id")
    @GetMapping("/{topicId}")
    TopicDTO getTopicById(@PathVariable Long topicId) {
        TopicDTO topic = service.getTopicById(topicId);
        return topic;
    }

    @UserRole
    @Operation(summary = "[USER] Create topic")
    @PostMapping("/create")
    ResponseEntity<TopicDTO> createTopic(@RequestBody @Valid CreateTopicDTO createTopicDTO, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        TopicDTO topicCreated = service.createTopic(createTopicDTO, username);
        return ResponseEntity.created(URI.create("/topics")).body(topicCreated);
    }

    @UserRole
    @Operation(summary = "[USER] Delete topic of authenticated user by topic id")
    @DeleteMapping("/delete/{topicId}")
    ResponseEntity<Void> deleteTopicByAuthor(@PathVariable Long topicId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteTopicByAuthor(topicId, username);
        return ResponseEntity.noContent().build();
    }

    @UserRole
    @Operation(summary = "[USER] Close topic of authenticated user by topic id")
    @PatchMapping("/close-topic/{topicId}")
    TopicDTO closeTopicByAuthor(@PathVariable Long topicId, @RequestBody @Valid CloseReasonDTO closeReasonDTO,
                                Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        TopicDTO topicClosed = service.closeTopicByAuthor(topicId, closeReasonDTO, username);
        return topicClosed;
    }

    @UserRole
    @Operation(summary = "[USER] Edit topic of authenticated user by topic id")
    @PatchMapping("/edit/{topicId}")
    TopicDTO updateTopicByAuthor(@RequestBody UpdateTopicDTO updateTopicDTO, @PathVariable Long topicId,
                                 Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        TopicDTO topicChanged = service.updateTopicByAuthor(topicId, updateTopicDTO, username);
        return topicChanged;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Edit topic by topic id")
    @PatchMapping("/moderator/edit/{topicId}")
    TopicDTO updateTopicByModerator(@RequestBody UpdateTopicDTO updateTopicDTO, @PathVariable Long topicId,
                                    Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        TopicDTO topicChanged = service.updateTopicByModerator(topicId, updateTopicDTO, moderatorName);
        return topicChanged;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Change category by topic id")
    @PatchMapping("/moderator/change-category/{topicId}")
    TopicDTO changeCategoryOfTopic(@RequestBody @Valid UpdateTopicCategoryDTO updateTopicCategoryDTO,
                                                              @PathVariable Long topicId, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        TopicDTO topicChanged = service.changeCategoryOfTopic(topicId, updateTopicCategoryDTO, moderatorName);
        return topicChanged;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Close topic by topic id")
    @PatchMapping("/moderator/close-topic/{topicId}")
    TopicDTO closeTopicByModerator(@PathVariable Long topicId, @RequestBody @Valid CloseReasonDTO closeReasonDTO,
                                            Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        TopicDTO topicClosed = service.closeTopicByModerator(topicId, closeReasonDTO, moderatorName);
        return topicClosed;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Delete topic by topic ids")
    @DeleteMapping("/moderator/delete/{topicId}")
    ResponseEntity<Void> deleteTopicByModerator(@PathVariable Long topicId, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        service.deleteTopicByModerator(topicId, moderatorName);
        return ResponseEntity.noContent().build();
    }
}