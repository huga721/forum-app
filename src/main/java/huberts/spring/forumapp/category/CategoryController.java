package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.*;
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

    @GetMapping("/{categoryId}")
    ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
        CategoryDTO category = service.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }

    @ModeratorRole
    @PostMapping("/moderator/create")
    ResponseEntity<CategoryDTO> saveNewCategory(@RequestBody @Valid CreateCategoryDTO createCategoryDTO) {
        CategoryDTO categoryCreated = service.createCategory(createCategoryDTO);
        return ResponseEntity.created(URI.create("/categories")).body(categoryCreated);
    }

    @AdminRole
    @PatchMapping("/admin/edit/title/{categoryId}")
    ResponseEntity<CategoryDTO> editTitle(@PathVariable Long categoryId,
                                          @RequestBody @Valid UpdateCategoryTitleDTO updateCategoryTitleDTO) {
        CategoryDTO categoryEdited = service.updateTitle(categoryId, updateCategoryTitleDTO);
        return ResponseEntity.ok(categoryEdited);
    }

    @AdminRole
    @PatchMapping("/admin/edit/description/{categoryId}")
    ResponseEntity<CategoryDTO> editDescription(@PathVariable Long categoryId,
                                                @RequestBody @Valid UpdateCategoryDescriptionDTO updateCategoryDescriptionDTO) {
        CategoryDTO categoryEdited = service.updateDescription(categoryId, updateCategoryDescriptionDTO);
        return ResponseEntity.ok(categoryEdited);
    }

    @AdminRole
    @DeleteMapping("/admin/delete/{categoryId}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}