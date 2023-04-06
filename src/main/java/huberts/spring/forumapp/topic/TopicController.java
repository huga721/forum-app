package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import huberts.spring.forumapp.security.annotation.UserRole;
import huberts.spring.forumapp.topic.dto.*;
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
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService service;

    @GetMapping()
    ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<TopicDTO> topics = service.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    @GetMapping("/{topicId}")
    ResponseEntity<TopicDTO> getById(@PathVariable Long topicId) {
        TopicDTO topic = service.getTopicById(topicId);
        return ResponseEntity.ok(topic);
    }

    @UserRole
    @PostMapping("/create")
    ResponseEntity<TopicDTO> saveNewTopic(@RequestBody @Valid TopicCreateDTO create, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        TopicDTO topicCreated = service.createTopic(create, username);
        return ResponseEntity.created(URI.create("/topics")).body(topicCreated);
    }

    @UserRole
    @DeleteMapping("/delete/{topicId}")
    ResponseEntity<Void> deleteTopicByAuthor(@PathVariable Long topicId, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteTopicByAuthor(topicId, username);
        return ResponseEntity.noContent().build();
    }

    @UserRole
    @PatchMapping("/close-topic/{topicId}")
    ResponseEntity<TopicDTO> closeTopicByAuthor(@PathVariable Long topicId, @RequestBody @Valid CloseReasonDTO closeTopicDTO,
                                                Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        return ResponseEntity.ok(service.closeTopicByAuthor(topicId, closeTopicDTO, username));
    }

    @UserRole
    @PatchMapping("/edit/{topicId}")
    ResponseEntity<TopicDTO> changeContentOrTitleByAuthor(@RequestBody TopicEditDTO topicEditDTO, @PathVariable Long topicId,
                                                          Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        TopicDTO topicChanged = service.updateTopicByAuthor(topicId, topicEditDTO, username);
        return ResponseEntity.ok(topicChanged);
    }

    @ModeratorRole
    @PatchMapping("/moderator/edit/{topicId}")
    ResponseEntity<TopicDTO> changeContentOrTitleByModerator(@RequestBody TopicEditDTO topicEditDTO,
                                                             @PathVariable Long topicId, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        TopicDTO topicChanged = service.updateTopicByModerator(topicId, topicEditDTO, moderatorName);
        return ResponseEntity.ok(topicChanged);
    }

    @ModeratorRole
    @PatchMapping("/moderator/change-category/{topicId}")
    ResponseEntity<TopicDTO> changeCategoryOfTopicByModerator(@RequestBody @Valid CategoryTitleDTO categoryTitleDTO,
                                                              @PathVariable Long topicId, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        TopicDTO topicChanged = service.changeCategoryOfTopic(topicId, categoryTitleDTO, moderatorName);
        return ResponseEntity.ok(topicChanged);
    }

    @ModeratorRole
    @PatchMapping("/moderator/close-topic/{topicId}")
    ResponseEntity<TopicDTO> closeTopicById(@PathVariable Long topicId, @RequestBody @Valid CloseReasonDTO closeTopicDTO,
                                            Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        return ResponseEntity.ok(service.closeTopicByModerator(topicId, closeTopicDTO, moderatorName));
    }

    @ModeratorRole
    @DeleteMapping("/moderator/delete/{topicId}")
    ResponseEntity<Void> deleteTopicByModerator(@PathVariable Long topicId, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        service.deleteTopicByModerator(topicId, moderatorName);
        return ResponseEntity.noContent().build();
    }
}