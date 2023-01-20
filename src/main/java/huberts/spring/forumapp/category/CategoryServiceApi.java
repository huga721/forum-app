package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CreateDTO;

import java.util.List;

public interface CategoryServiceApi {
    CategoryDTO createCategory (CreateDTO category);

    List<Category> findAll ();
}
