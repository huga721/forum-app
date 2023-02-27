package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.security.annotation.AdminRole;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService service;

    @GetMapping("/all")
    ResponseEntity<List<CategoryDTO>> getAll() {
        List<CategoryDTO> categories = service.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{title}")
    ResponseEntity<CategoryDTO> getByTitle(@PathVariable String title) {
        CategoryDTO category = service.findCategoryDTO(title);
        return ResponseEntity.ok(category);
    }

    @ModeratorRole
    @PostMapping("/moderator/create")
    ResponseEntity<CategoryDTO> create(@RequestBody CategoryCreateDTO create) {
        CategoryDTO category = service.saveNewCategory(create);
        return ResponseEntity.created(URI.create("/categories")).body(category);
    }

    @AdminRole
    @PatchMapping("/admin/{title}/title")
    ResponseEntity<CategoryDTO> changeTitle(@PathVariable String title, @RequestBody CategoryTitleDTO newTitle) {
        CategoryDTO category = service.changeTitle(title, newTitle);
        return ResponseEntity.ok(category);
    }

    @AdminRole
    @PatchMapping("/admin/{title}/description")
    ResponseEntity<CategoryDTO> changeDescription(@PathVariable String title, @RequestBody CategoryDescriptionDTO description) {
        CategoryDTO category = service.changeDescription(title, description);
        return ResponseEntity.ok(category);
    }

    @AdminRole
    @DeleteMapping("/admin/{title}/delete")
    ResponseEntity<Void> deleteCategory(@PathVariable String title) {
        service.deleteCategory(title);
        return ResponseEntity.noContent().build();
    }
}