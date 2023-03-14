package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;

import java.util.List;

public interface CategoryServiceApi {
    CategoryDTO createCategory(CategoryCreateDTO category);

    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getAllCategories();

    CategoryDTO updateTitle(Long id, CategoryTitleDTO newCategoryTitle);
    CategoryDTO updateDescription(Long id, CategoryDescriptionDTO newDescription);

    void deleteCategory(Long id);
}