package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;

import java.util.List;

public interface CategoryServiceApi {
    Category findCategory(String title);
    CategoryDTO saveNewCategory(CategoryCreateDTO category);
    List<CategoryDTO> findAll();
    CategoryDTO findCategoryDTO(String title);
    CategoryDTO changeTitle(String existingCategory, CategoryTitleDTO newCategoryTitle);
    void deleteCategory(String title);
    CategoryDTO changeDescription(String title, CategoryDescriptionDTO newDescription);
}