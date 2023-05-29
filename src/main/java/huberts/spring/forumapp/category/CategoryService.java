package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.*;
import huberts.spring.forumapp.exception.category.CategoryAlreadyExistException;
import huberts.spring.forumapp.exception.category.CategoryDescriptionException;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
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

    private static final String CATEGORY_DOESNT_EXIST_EXCEPTION = "Category with title \"%s\" doesn't exist.";
    private static final String CATEGORY_ALREADY_EXISTS_EXCEPTION = "Category \"%s\" already exists, please choose another title.";
    private static final String TITLE_IS_THE_SAME_EXCEPTION = "New title is the same as actual title in category.";
    private static final String DESCRIPTION_ARE_THE_SAME_EXCEPTION = "New description is the same as actual description in category.";
    private static final String EXCEPTION_OCCURRED = "An exception occurred!";

    private final CategoryRepository repository;

    @Override
    public CategoryDTO createCategory(CreateCategoryDTO createCategoryDTO) {
        log.info("Creating category with title {}", createCategoryDTO.title());
        if (repository.existsByTitle(createCategoryDTO.title())) {
            String errorMessage = String.format(CATEGORY_ALREADY_EXISTS_EXCEPTION, createCategoryDTO.title());
            log.error(EXCEPTION_OCCURRED, new CategoryAlreadyExistException(errorMessage));
            throw new CategoryAlreadyExistException(String.format(errorMessage));
        }
        log.info("Category created");
        return buildAndSaveCategory(createCategoryDTO);
    }

    private CategoryDTO buildAndSaveCategory(CreateCategoryDTO createCategoryDTO) {
        Category categoryBuilt = CategoryMapper.buildNewCategory(createCategoryDTO);
        repository.save(categoryBuilt);
        return CategoryMapper.buildCategoryDTO(categoryBuilt);
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        log.info("Getting category with id {}", categoryId);
        Category categoryFound = findCategoryById(categoryId);
        return CategoryMapper.buildCategoryDTO(categoryFound);
    }

    private Category findCategoryById(Long categoryId) {
        log.info("Finding category with id {}", categoryId);
        return repository.findById(categoryId)
                .orElseThrow(() -> {
                    String errorMessage = String.format(CATEGORY_DOESNT_EXIST_EXCEPTION, categoryId);
                    log.error(EXCEPTION_OCCURRED, new CategoryDoesntExistException(errorMessage));
                    return new CategoryDoesntExistException(errorMessage);
                });
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        log.info("Getting all categories");
        return CategoryMapper.mapFromListCategory(repository.findAll());
    }

    @Override
    public CategoryDTO updateTitle(Long categoryId, NewCategoryTitleDTO newCategoryTitleDTO) {
        log.info("Updating title of category with id {} by moderator or admin", categoryId);
        Category categoryFound = findCategoryById(categoryId);
        String title = categoryFound.getTitle();
        String newTitle = newCategoryTitleDTO.title();

        if (title.equals(newTitle)) {
            log.error(EXCEPTION_OCCURRED, new CategoryTitleException(TITLE_IS_THE_SAME_EXCEPTION));
            throw new CategoryTitleException(TITLE_IS_THE_SAME_EXCEPTION);
        }
        if (repository.existsByTitle(newTitle)) {
            String errorMessage = String.format(CATEGORY_ALREADY_EXISTS_EXCEPTION, newTitle);
            log.error(EXCEPTION_OCCURRED, new CategoryTitleException(String.format(errorMessage)));
            throw new CategoryTitleException(String.format(errorMessage));
        }

        categoryFound.setTitle(newTitle);
        log.info("Category updated");
        return CategoryMapper.buildCategoryDTO(categoryFound);
    }

    @Override
    public CategoryDTO updateDescription(Long categoryId, NewCategoryDescriptionDTO newCategoryDescriptionDTO) {
        log.info("Updating description of category with id {} by moderator or admin", categoryId);
        Category categoryFound = findCategoryById(categoryId);
        String description = categoryFound.getDescription();
        String newDescription = newCategoryDescriptionDTO.description();

        if (newDescription.equals(description)) {
            log.error(EXCEPTION_OCCURRED, new CategoryDescriptionException(DESCRIPTION_ARE_THE_SAME_EXCEPTION));
            throw new CategoryDescriptionException(DESCRIPTION_ARE_THE_SAME_EXCEPTION);
        }

        categoryFound.setDescription(newDescription);
        log.info("Category updated");
        return CategoryMapper.buildCategoryDTO(categoryFound);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Deleting category with id {}", categoryId);
        Category categoryFound = findCategoryById(categoryId);
        repository.delete(categoryFound);
        log.info("Deleted topic");
    }
}