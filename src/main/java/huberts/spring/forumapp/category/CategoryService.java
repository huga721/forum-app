package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.exception.category.CategoryAlreadyExistingException;
import huberts.spring.forumapp.exception.category.CategoryDescriptionException;
import huberts.spring.forumapp.exception.category.CategoryExistException;
import huberts.spring.forumapp.exception.category.CategoryTitleException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceApi {

    private final CategoryRepository repository;

    private static final String CATEGORY_DOESNT_EXIST_EXCEPTION = "Category with title \"%s\" doesn't exist.";
    private static final String CATEGORY_ALREADY_EXISTS_EXCEPTION = "Category \"%s\" already exists, please choose another title.";
    private static final String TITLE_IS_THE_SAME_EXCEPTION = "New title is the same as actual title in category.";
    private static final String DESCRIPTION_ARE_THE_SAME_EXCEPTION = "New description is the same as actual description in category.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    @Override
    public CategoryDTO createCategory(CategoryCreateDTO category) {
        String title = category.getTitle();
        log.info("Creating category with title {}", title);

        if (repository.existsByTitle(title)) {
            String errorMessage = String.format(CATEGORY_ALREADY_EXISTS_EXCEPTION, title);
            log.error(EXCEPTION_OCCURRED, new CategoryAlreadyExistingException(errorMessage));
            throw new CategoryAlreadyExistingException(String.format(errorMessage));
        }

        Category categoryBuilt = CategoryMapper.buildNewCategory(category);
        Category categorySaved = repository.save(categoryBuilt);

        log.info("Category created");
        return CategoryMapper.buildCategoryDTO(categorySaved);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        log.info("Getting category with id {}", id);
        Category categoryFound = findCategory(id);
        return CategoryMapper.buildCategoryDTO(categoryFound);
    }

    private Category findCategory(Long id) {
        log.info("Finding category with id id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format(CATEGORY_DOESNT_EXIST_EXCEPTION, id);
                    log.error(EXCEPTION_OCCURRED, new CategoryExistException(errorMessage));
                    return new CategoryExistException(errorMessage);
                });
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        log.info("Getting all categories");
        return CategoryMapper.mapFromListCategory(repository.findAll());
    }

    @Override
    public CategoryDTO updateTitle(Long id, CategoryTitleDTO newTitleDTO) {
        log.info("Updating title of category with id {} by moderator or admin", id);
        String newTitle = newTitleDTO.getCategoryTitle();

        Category categoryToUpdate = findCategory(id);
        String categoryTitle = categoryToUpdate.getTitle();

        if (categoryTitle.equals(newTitle)) {
            log.error(EXCEPTION_OCCURRED, new CategoryTitleException(TITLE_IS_THE_SAME_EXCEPTION));
            throw new CategoryTitleException(TITLE_IS_THE_SAME_EXCEPTION);
        }
        if (repository.existsByTitle(newTitle)) {
            String errorMessage = String.format(CATEGORY_ALREADY_EXISTS_EXCEPTION, newTitle);
            log.error(EXCEPTION_OCCURRED, new CategoryTitleException(String.format(errorMessage)));
            throw new CategoryTitleException(String.format(errorMessage));
        }

        categoryToUpdate.setTitle(newTitle);
        log.info("Category updated");
        return CategoryMapper.buildCategoryDTO(categoryToUpdate);
    }

    @Override
    public CategoryDTO updateDescription(Long id, CategoryDescriptionDTO newCategoryDescription) {
        log.info("Updating description of category with id {} by moderator or admin", id);
        String newDescription = newCategoryDescription.getDescription();

        Category categoryToUpdate = findCategory(id);
        String categoryDescription = categoryToUpdate.getDescription();

        if (newDescription.equals(categoryDescription)) {
            log.error(EXCEPTION_OCCURRED, new CategoryDescriptionException(DESCRIPTION_ARE_THE_SAME_EXCEPTION));
            throw new CategoryDescriptionException(DESCRIPTION_ARE_THE_SAME_EXCEPTION);
        }

        categoryToUpdate.setDescription(newDescription);
        log.info("Category updated");
        return CategoryMapper.buildCategoryDTO(categoryToUpdate);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with id {}", id);
        Category categoryFound = findCategory(id);
        repository.delete(categoryFound);
        log.info("Deleted topic");
    }
}