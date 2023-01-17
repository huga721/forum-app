package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;

import java.util.List;

public interface CategoryServiceApi {
    CategoryDTO createCategory (CategoryDTO category);

    List<Category> findAll ();
}
