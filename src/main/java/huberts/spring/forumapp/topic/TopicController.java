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

    @GetMapping("/{id}")
    ResponseEntity<TopicDTO> getById(@PathVariable Long id) {
        TopicDTO topic = service.getTopicById(id);
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
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteTopicByAuthor(@PathVariable Long id, Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        service.deleteTopicByAuthor(id, username);
        return ResponseEntity.noContent().build();
    }

    @UserRole
    @PatchMapping("/edit/{id}")
    ResponseEntity<TopicDTO> changeContentOrTitleByAuthor(@RequestBody TopicEditDTO topicEditDTO, @PathVariable Long id,
                                           Authentication authenticatedUser) {
        String username = authenticatedUser.getName();
        TopicDTO topicChanged = service.updateTopicByAuthor(id, topicEditDTO, username);
        return ResponseEntity.ok(topicChanged);
    }

    @ModeratorRole
    @PatchMapping("/moderator/edit/{id}")
    ResponseEntity<TopicDTO> changeContentOrTitleByModerator(@RequestBody TopicEditDTO topicEditDTO,
                                                      @PathVariable Long id, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        TopicDTO topicChanged = service.updateTopicByModerator(id, topicEditDTO, moderatorName);
        return ResponseEntity.ok(topicChanged);
    }

    @ModeratorRole
    @PatchMapping("/moderator/change-category/{id}")
    ResponseEntity<TopicDTO> changeCategoryOfTopicByModerator(@RequestBody @Valid CategoryTitleDTO categoryTitleDTO,
                                                              @PathVariable Long id, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        TopicDTO topicChanged = service.changeCategoryOfTopic(id, categoryTitleDTO, moderatorName);
        return ResponseEntity.ok(topicChanged);
    }

    @ModeratorRole
    @DeleteMapping("/moderator/delete/{id}")
    ResponseEntity<Void> deleteTopicByModerator(@PathVariable Long id, Authentication authenticatedUser) {
        String moderatorName = authenticatedUser.getName();
        service.deleteTopicByModerator(id, moderatorName);
        return ResponseEntity.noContent().build();
    }
}