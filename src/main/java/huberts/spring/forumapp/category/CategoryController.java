package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.*;
import huberts.spring.forumapp.security.annotation.AdminRole;
import huberts.spring.forumapp.security.annotation.ModeratorRole;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService service;

    @Operation(summary = "Get all categories")
    @GetMapping()
    List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categories = service.getAllCategories();
        return categories;
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{categoryId}")
    CategoryDTO getCategoryById(@PathVariable Long categoryId) {
        CategoryDTO category = service.getCategoryById(categoryId);
        return category;
    }

    @ModeratorRole
    @Operation(summary = "[MODERATOR] Create category")
    @PostMapping("/moderator/create")
    ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateCategoryDTO createCategoryDTO) {
        CategoryDTO categoryCreated = service.createCategory(createCategoryDTO);
        return ResponseEntity.created(URI.create("/categories")).body(categoryCreated);
    }

    @AdminRole
    @Operation(summary = "[ADMIN] Update title by category id")
    @PatchMapping("/admin/edit/title/{categoryId}")
    CategoryDTO updateTitle(@PathVariable Long categoryId, @RequestBody @Valid NewCategoryTitleDTO newCategoryTitleDTO) {
        CategoryDTO categoryEdited = service.updateTitle(categoryId, newCategoryTitleDTO);
        return categoryEdited;
    }

    @AdminRole
    @Operation(summary = "[ADMIN] Update description by category id")
    @PatchMapping("/admin/edit/description/{categoryId}")
    CategoryDTO updateDescription(@PathVariable Long categoryId, @RequestBody @Valid NewCategoryDescriptionDTO newCategoryDescriptionDTO) {
        CategoryDTO categoryEdited = service.updateDescription(categoryId, newCategoryDescriptionDTO);
        return categoryEdited;
    }

    @AdminRole
    @Operation(summary = "[ADMIN] Delete category by category id")
    @DeleteMapping("/admin/delete/{categoryId}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}