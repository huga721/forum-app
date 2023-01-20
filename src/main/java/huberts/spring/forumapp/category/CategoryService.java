package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CreateDTO;
import huberts.spring.forumapp.exception.CategoryAlreadyExistingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceApi {
    private final CategoryRepository repository;

    @Override
    public CategoryDTO createCategory(CreateDTO category) {
        String title = category.getTitle();
        if (repository.existsByTitle(title)) {
            throw new CategoryAlreadyExistingException("Category " + category.getTitle() +
                    " already exists, please choose another title.");
        }

        Category categoryCreated = categoryBuilder(title);
        repository.save(categoryCreated);

        return categoryDTOBuilder(categoryCreated);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    private static Category categoryBuilder (String title) {
        return Category.builder()
                .title(title)
                .posts(null)
                .build();
    }
    private static CategoryDTO categoryDTOBuilder (Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .title(category.getTitle())
                .posts(category.getPosts())
                .build();
    }
}
