package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.*;
import huberts.spring.forumapp.exception.category.CategoryAlreadyExistException;
import huberts.spring.forumapp.exception.category.CategoryDescriptionException;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.category.CategoryTitleException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private static final String TITLE = "test title of category";
    private static final String TITLE_NEW = "title for list hehe";
    private static final String DESCRIPTION = "test description of category";
    private static final String DESCRIPTION_NEW = "description new for testing purposes";

    @Mock
    private CategoryRepository repository;
    @InjectMocks
    private CategoryService service;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .topics(List.of())
                .build();
    }

    @DisplayName("createCategory method")
    @Nested
    class CreateCategoryTests {

        @DisplayName("Should create category")
        @Test
        void shouldCreateCategory() {
            CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO(TITLE, DESCRIPTION);
            when(repository.existsByTitle(any(String.class))).thenReturn(false);
            when(repository.save(any(Category.class))).thenReturn(category);
            CategoryDTO category = service.createCategory(createCategoryDTO);

            assertNotNull(category);
            verify(repository, times(1)).save(any(Category.class));
        }

        @DisplayName("Should throw CategoryAlreadyExistingException when category exists")
        @Test
        void shouldThrowCategoryAlreadyExistException_WhenCategoryExists() {
            CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO(TITLE, DESCRIPTION);
            when(repository.existsByTitle(any(String.class))).thenReturn(true);
            assertThrows(CategoryAlreadyExistException.class, () -> service.createCategory(createCategoryDTO));
        }
    }

    @DisplayName("getCategoryById method")
    @Nested
    class GetCategoryByIdTests {

        @DisplayName("Should get category by id")
        @Test
        void shouldGetCategoryById() {
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            CategoryDTO result = service.getCategoryById(1L);

            assertNotNull(result);
            verify(repository, times(1)).findById(any(Long.class));
        }

        @DisplayName("Should throw CategoryExistException when category with given id doesn't exist")
        @Test
        void shouldThrowCategoryExistException_WhenCategoryWithGivenIdDoesntExist() {
            assertThrows(CategoryDoesntExistException.class, () -> service.getCategoryById(1L));
        }
    }

    @DisplayName("getAllCategories method")
    @Nested
    class GetAllCategoriesTests {

        @DisplayName("Should return list of categories")
        @Test
        void shouldReturnListOfCategories() {
            List<Category> categories = List.of(category);
            given(repository.findAll()).willReturn(categories);
            List<CategoryDTO> categoriesResult = service.getAllCategories();

            assertNotNull(categoriesResult);
            verify(repository, times(1)).findAll();
        }
    }

    @DisplayName("updateTitle method")
    @Nested
    class UpdateTitleTests {

        @DisplayName("Should change title")
        @Test
        void shouldChangeTitle() {
            NewCategoryTitleDTO titleToUpdate = new NewCategoryTitleDTO(TITLE_NEW);

            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            when(repository.existsByTitle(any(String.class))).thenReturn(false);
            CategoryDTO result = service.updateTitle(1L, titleToUpdate);

            assertEquals(result.title(), titleToUpdate.title());
        }

        @DisplayName("Should throw CategoryExistException when title category to update doesn't exist")
        @Test
        void shouldThrowCategoryExistException_WhenTitleCategoryToUpdateDoesntExist() {
            NewCategoryTitleDTO titleToUpdate = new NewCategoryTitleDTO(TITLE_NEW);
            assertThrows(CategoryDoesntExistException.class, () -> service.updateTitle(1L, titleToUpdate));
        }

        @DisplayName("Should throw CategoryTitleException when title to change is the same as actual title")
        @Test
        void shouldThrowCategoryTitleException_WhenTitleToChangeIsTheSameAsActualTitle() {
            NewCategoryTitleDTO titleToThrow = new NewCategoryTitleDTO(TITLE);
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            assertThrows(CategoryTitleException.class, () -> service.updateTitle(1L, titleToThrow));
        }

        @DisplayName("Should throw CategoryTitleException when category with title to update exist")
        @Test
        void shouldThrowCategoryTitleException_WhenCategoryWithTitleToUpdateExist() {
            NewCategoryTitleDTO titleToUpdate = new NewCategoryTitleDTO(TITLE_NEW);
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            when(repository.existsByTitle(any(String.class))).thenReturn(true);

            assertThrows(CategoryTitleException.class, () -> service.updateTitle(1L, titleToUpdate));
        }
    }

    @DisplayName("updateDescription method")
    @Nested
    class UpdateDescriptionTests {

        @DisplayName("Should update description")
        @Test
        void shouldUpdateDescription() {
            NewCategoryDescriptionDTO descriptionToUpdate = new NewCategoryDescriptionDTO(DESCRIPTION_NEW);

            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            CategoryDTO result = service.updateDescription(1L, descriptionToUpdate);

            assertNotNull(result);
            assertEquals(category.getDescription(), descriptionToUpdate.description());
        }

        @DisplayName("Should throw CategoryExistException when category doesn't exist")
        @Test
        void shouldThrowCategoryExistException_WhenCategoryDoesntExist() {
            NewCategoryDescriptionDTO descriptionToUpdate = new NewCategoryDescriptionDTO(DESCRIPTION_NEW);
            assertThrows(CategoryDoesntExistException.class, () -> service.updateDescription(1L, descriptionToUpdate));
        }

        @DisplayName("Should throw CategoryDescriptionException when new description is the same as it is")
        @Test
        void shouldThrowCategoryDescriptionException_WhenNewDescriptionIsTheSameAsItIs() {
            NewCategoryDescriptionDTO descriptionToThrow = new NewCategoryDescriptionDTO(DESCRIPTION);
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            assertThrows(CategoryDescriptionException.class, () -> service.updateDescription(1L, descriptionToThrow));
        }
    }

    @DisplayName("deleteCategory method")
    @Nested
    class DeleteCategoryTests {

        @DisplayName("Should delete category")
        @Test
        void shouldDeleteCategory() {
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            service.deleteCategory(1L);
            verify(repository, times(1)).delete(category);
        }

        @DisplayName("Should throw CategoryExistException when category doesn't exist")
        @Test
        void shouldThrowCategoryExistException_WhenCategoryDoesntExist() {
            assertThrows(CategoryDoesntExistException.class, () -> service.deleteCategory(1L));
        }
    }
}