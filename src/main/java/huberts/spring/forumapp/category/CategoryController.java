package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.security.annotation.AdminRole;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;

    @GetMapping()
    ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = service.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @ModeratorRole
    @PostMapping("/moderator/create")
    ResponseEntity<CategoryDTO> saveNewCategory(@RequestBody @Valid CategoryCreateDTO create) {
        CategoryDTO categoryCreated = service.createCategory(create);
        return ResponseEntity.created(URI.create("/categories")).body(categoryCreated);
    }

    @AdminRole
    @PatchMapping("/admin/edit/title/{id}")
    ResponseEntity<CategoryDTO> editTitle(@PathVariable Long id, @RequestBody @Valid CategoryTitleDTO newTitle) {
        CategoryDTO categoryEdited = service.updateTitle(id, newTitle);
        return ResponseEntity.ok(categoryEdited);
    }

    @AdminRole
    @PatchMapping("/admin/edit/description/{id}")
    ResponseEntity<CategoryDTO> editDescription(@PathVariable Long id, @RequestBody @Valid CategoryDescriptionDTO description) {
        CategoryDTO categoryEdited = service.updateDescription(id, description);
        return ResponseEntity.ok(categoryEdited);
    }

    @AdminRole
    @DeleteMapping("/admin/delete/{id}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}