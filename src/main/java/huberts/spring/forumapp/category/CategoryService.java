package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.exception.CategoryAlreadyExistingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceApi {
    private final CategoryRepository repository;

    @Override
    public CategoryDTO createCategory(CategoryDTO category) {
        String title = category.getTitle();
        if (repository.existsByTitle(title)) {
            throw new CategoryAlreadyExistingException("Category " + category.getTitle() +
                    " already exists, please choose another title.");
        }

        Category categoryCreated = categoryBuilder(title);
        repository.save(categoryCreated);

        return categoryDTOBuilder(title);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    private static Category categoryBuilder (String title) {
        return Category.builder()
                .title(title)
                .build();
    }
    private static CategoryDTO categoryDTOBuilder (String title) {
        return CategoryDTO.builder()
                .title(title)
                .build();
    }
}
