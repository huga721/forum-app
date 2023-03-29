package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.exception.category.CategoryAlreadyExistException;
import huberts.spring.forumapp.exception.category.CategoryDescriptionException;
import huberts.spring.forumapp.exception.category.CategoryExistException;
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

    public static final String TITLE = "test title of category";
    public static final String TITLE_NEW = "title for list hehe";
    public static final String DESCRIPTION = "test description of category";
    public static final String DESCRIPTION_NEW = "description new for testing purposes";

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

        private CategoryCreateDTO createCategoryDTO;

        @BeforeEach
        void setUp() {
            createCategoryDTO = new CategoryCreateDTO(TITLE, DESCRIPTION);
        }

        @DisplayName("Should create category")
        @Test
        void shouldCreateCategory() {
            when(repository.existsByTitle(any(String.class))).thenReturn(false);
            when(repository.save(any(Category.class))).thenReturn(category);
            CategoryDTO category = service.createCategory(createCategoryDTO);

            assertNotNull(category);
            verify(repository, times(1)).save(any(Category.class));
        }

        @DisplayName("Should throw CategoryAlreadyExistingException when category exists")
        @Test
        void shouldThrowCategoryAlreadyExistException_WhenCategoryExists() {
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
            assertThrows(CategoryExistException.class, () -> service.getCategoryById(1L));
        }
    }

    @DisplayName("getAllCategories method")
    @Nested
    class GetAllCategoriesTests {

        private static List<Category> categories;

        @BeforeEach
        void setUp() {
            categories = List.of(category);
        }

        @DisplayName("Should return list of categories")
        @Test
        void shouldReturnListOfCategories() {
            given(repository.findAll()).willReturn(categories);
            List<CategoryDTO> categoriesResult = service.getAllCategories();

            assertNotNull(categoriesResult);
            verify(repository, times(1)).findAll();
        }
    }

    @DisplayName("updateTitle method")
    @Nested
    class UpdateTitleTests {

        private CategoryTitleDTO titleToUpdate;
        private CategoryTitleDTO titleToThrow;

        @BeforeEach
        void setUp() {
            titleToUpdate = new CategoryTitleDTO(TITLE_NEW);
            titleToThrow = new CategoryTitleDTO(TITLE);
        }

        @DisplayName("Should change title")
        @Test
        void shouldChangeTitle() {
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            when(repository.existsByTitle(any(String.class))).thenReturn(false);
            CategoryDTO result = service.updateTitle(1L, titleToUpdate);

            assertEquals(result.getTitle(), titleToUpdate.getCategoryTitle());
        }

        @DisplayName("Should throw CategoryExistException when title category to update doesn't exist")
        @Test
        void shouldThrowCategoryExistException_WhenTitleCategoryToUpdateDoesntExist() {
            assertThrows(CategoryExistException.class, () -> service.updateTitle(1L, titleToUpdate));
        }

        @DisplayName("Should throw CategoryTitleException when title to change is the same as actual title")
        @Test
        void shouldThrowCategoryTitleException_WhenTitleToChangeIsTheSameAsActualTitle() {
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));

            assertThrows(CategoryTitleException.class, () -> service.updateTitle(1L, titleToThrow));
        }

        @DisplayName("Should throw CategoryTitleException when category with title to update exist")
        @Test
        void shouldThrowCategoryTitleException_WhenCategoryWithTitleToUpdateExist() {
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            when(repository.existsByTitle(any(String.class))).thenReturn(true);

            assertThrows(CategoryTitleException.class, () -> service.updateTitle(1L, titleToUpdate));
        }
    }

    @DisplayName("updateDescription method")
    @Nested
    class UpdateDescriptionTests {

        private CategoryDescriptionDTO descriptionToUpdate;
        private CategoryDescriptionDTO descriptionToThrow;

        @BeforeEach
        void setUp() {
            descriptionToUpdate = CategoryDescriptionDTO.builder()
                    .description(DESCRIPTION_NEW)
                    .build();
            descriptionToThrow = CategoryDescriptionDTO.builder()
                    .description(DESCRIPTION)
                    .build();
        }

        @DisplayName("Should update description")
        @Test
        void shouldUpdateDescription() {
            when(repository.findById(any(Long.class))).thenReturn(Optional.of(category));
            CategoryDTO result = service.updateDescription(1L, descriptionToUpdate);

            assertNotNull(result);
            assertEquals(category.getDescription(), descriptionToUpdate.getDescription());
        }

        @DisplayName("Should throw CategoryExistException when category doesn't exist")
        @Test
        void shouldThrowCategoryExistException_WhenCategoryDoesntExist() {
            assertThrows(CategoryExistException.class, () -> service.updateDescription(1L, descriptionToUpdate));
        }

        @DisplayName("Should throw CategoryDescriptionException when new description is the same as it is")
        @Test
        void shouldThrowCategoryDescriptionException_WhenNewDescriptionIsTheSameAsItIs() {
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
            assertThrows(CategoryExistException.class, () -> service.deleteCategory(1L));
        }
    }
}