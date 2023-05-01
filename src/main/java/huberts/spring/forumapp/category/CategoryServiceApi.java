package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.*;

import java.util.List;

public interface CategoryServiceApi {
    CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO);

    CategoryDTO getCategoryById(Long categoryId);
    List<CategoryDTO> getAllCategories();

    CategoryDTO updateTitle(Long categoryId, NewCategoryTitleDTO updateCategoryTitleDTO);
    CategoryDTO updateDescription(Long categoryId, NewCategoryDescriptionDTO updateCategoryDescriptionDTO);

    void deleteCategory(Long categoryId);
}