package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.exception.CategoryAlreadyExistingException;
import huberts.spring.forumapp.exception.CategoryDescriptionException;
import huberts.spring.forumapp.exception.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.CategoryTitleException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceApi {

    private final CategoryRepository repository;

    @Override
    public CategoryDTO saveNewCategory(CategoryCreateDTO category) {
        String categoryTitle = category.getTitle();
        String categoryDescription = category.getDescription();

        if (categoryTitle.isBlank()) {
            throw new CategoryTitleException("Title is blank or empty.");
        }
        if (categoryDescription.isBlank()) {
            throw new CategoryDescriptionException("Description is blank or empty.");
        }
        if (repository.existsByTitle(categoryTitle)) {
            throw new CategoryAlreadyExistingException("Category " + categoryTitle +
                    " already exists, please choose another title.");
        }
        if (categoryTitle.endsWith(" ")) {
            throw new CategoryTitleException("Title can't end with white space.");
        }
        if (categoryDescription.endsWith(" ")) {
            throw new CategoryDescriptionException("Description can't end with white space.");
        }

        Category created = CategoryMapper.buildNewCategory(category);
        Category result = repository.save(created);

        return CategoryMapper.buildCategoryDTO(result);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return CategoryMapper.mapFromListCategory(repository.findAll());
    }

    @Override
    public CategoryDTO findCategoryDTO(String categoryTitle) {
        if (!repository.existsByTitle(categoryTitle)) {
            throw new CategoryDoesntExistException("Category " + categoryTitle + " doesnt exist.");
        }
        Category result = repository.findByTitle(categoryTitle);
        return CategoryMapper.buildCategoryDTO(result);
    }

    @Override
    public CategoryDTO changeTitle(String categoryTitle, CategoryTitleDTO newCategoryTitle) {
        String newTitle = newCategoryTitle.getTitle();

        if (newTitle.isBlank() || categoryTitle.isBlank()) {
            throw new CategoryTitleException("Title is blank or empty.");
        }
        if (categoryTitle.equals(newTitle)) {
            throw new CategoryTitleException("New title is the same as actual title in category.");
        }

        Category result = findCategory(categoryTitle);
        result.setTitle(newTitle);

        return CategoryMapper.buildCategoryDTO(result);
    }

    @Override
    public CategoryDTO changeDescription(String categoryTitle, CategoryDescriptionDTO newCategoryDescription) {
        String newDescription = newCategoryDescription.getDescription();

        if (newDescription.isBlank()) {
            throw new CategoryDescriptionException("Category description is blank or empty.");
        }

        Category category = findCategory(categoryTitle);
        String actualDescription = category.getDescription();

        if (newDescription.equals(actualDescription)) {
            throw new CategoryDescriptionException("Categories descriptions are the same.");
        }

        category.setDescription(newDescription);

        return CategoryMapper.buildCategoryDTO(category);
    }

    @Override
    public void deleteCategory(String categoryTitle) {
        Category result = findCategory(categoryTitle);
        repository.delete(result);
    }

    @Override
    public Category findCategory(String categoryTitle) {
        if (!repository.existsByTitle(categoryTitle)) {
            throw new CategoryDoesntExistException("Category " + categoryTitle + " doesnt exist.");
        }
        return repository.findByTitle(categoryTitle);
    }
}