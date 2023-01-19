package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CreateDTO;
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

    @PostMapping("/create")
    ResponseEntity<CategoryDTO> create (@RequestBody CreateDTO category) {
        CategoryDTO categoryCreated = service.createCategory(category);
        return ResponseEntity.created(URI.create("/categories")).body(categoryCreated);
    }

    @DeleteMapping("/delete/")
    ResponseEntity

    @GetMapping("/getAllCategories")
    ResponseEntity<List<Category>> getAll () {
        return ResponseEntity.ok(service.findAll());
    }
}
