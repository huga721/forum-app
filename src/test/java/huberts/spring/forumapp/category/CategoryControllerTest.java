package huberts.spring.forumapp.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.category.dto.CreateCategoryDTO;
import huberts.spring.forumapp.category.dto.NewCategoryDescriptionDTO;
import huberts.spring.forumapp.category.dto.UpdateTopicCategoryDTO;
import huberts.spring.forumapp.exception.category.CategoryAlreadyExistException;
import huberts.spring.forumapp.exception.category.CategoryDescriptionException;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.category.CategoryTitleException;
import huberts.spring.forumapp.jwt.JwtKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class CategoryControllerTest extends ContainerIT {

    private static final String TITLE_1 = "test title";
    private static final String TITLE_2 = "second test title";
    private static final String NEW_TITLE_FOR_CREATE = "new title test";
    private static final String NEW_TITLE = "new title that is original";
    private static final String TITLE_TO_CHANGE = "hehelol";
    private static final String DESCRIPTION = "test description";
    private static final String NEW_DESCRIPTION = "new description that is original";
    private static final String DESCRIPTION_TOO_LONG = "test description is too long test description is f " +
            "test description is too long test description is f";
    private static final String EMPTY = "";

    private static final String GET_ALL_CATEGORIES_ENDPOINT = "/categories";
    private static final String GET_CATEGORY_BY_ID_ENDPOINT = "/categories/1";
    private static final String GET_CATEGORY_BY_ID_DOESNT_EXIST_ENDPOINT = "/categories/999";
    private static final String CREATE_CATEGORY_ENDPOINT = "/categories/moderator/create";
    private static final String CHANGE_TITLE_ENDPOINT = "/categories/admin/edit/title/3";
    private static final String CHANGE_TITLE_ARE_THE_SAME_ENDPOINT = "/categories/admin/edit/title/1";
    private static final String CHANGE_TITLE_ID_DOESNT_EXIST_ENDPOINT = "/categories/admin/edit/title/999";
    private static final String CHANGE_DESCRIPTION_ENDPOINT = "/categories/admin/edit/description/4";
    private static final String CHANGE_DESCRIPTION_ARE_THE_SAME_ENDPOINT = "/categories/admin/edit/description/1";
    private static final String CHANGE_DESCRIPTION_ID_DOESNT_EXIST_ENDPOINT = "/categories/admin/edit/description/999";
    private static final String DELETE_CATEGORY_ENDPOINT = "/categories/admin/delete/5";
    private static final String DELETE_CATEGORY_ID_DOESNT_EXIST_ENDPOINT = "/categories/admin/delete/999";

    private static final String TITLE_ARRAY_0_JSON_PATH = "$.[0].title";
    private static final String TITLE_ARRAY_1_JSON_PATH = "$.[1].title";
    private static final String TITLE_JSON_PATH = "$.title";
    private static final String DESCRIPTION_JSON_PATH = "$.description";

    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_TOKEN = "wrong_token_123";
    private static final String LOCATION = "location";
    private static final String NEW_CATEGORY_LOCATION = "/categories";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("get /categories endpoint")
    @Nested
    class CategoriesTests {

        @DisplayName("Should return list of all categories")
        @Test
        void shouldReturnListOfAllCategories() throws Exception {
            mockMvc.perform(get(GET_ALL_CATEGORIES_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(TITLE_ARRAY_0_JSON_PATH).value(TITLE_1))
                    .andExpect(jsonPath(TITLE_ARRAY_1_JSON_PATH).value(TITLE_2));
        }
    }

    @DisplayName("get /{id} endpoint")
    @Nested
    class IdTests {

        @DisplayName("Should return category, HTTP status 200")
        @Test
        void shouldReturnCategory() throws Exception {
            mockMvc.perform(get(GET_CATEGORY_BY_ID_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(TITLE_JSON_PATH).value(TITLE_1));
        }

        @DisplayName("Should throw CategoryDoesntExistException when category doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenCategoryDoesntExist() throws Exception {
            mockMvc.perform(get(GET_CATEGORY_BY_ID_DOESNT_EXIST_ENDPOINT))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesntExistException));
        }
    }

    @DisplayName("post /moderator/create endpoint")
    @Nested
    class ModeratorCreateTests {

        @DisplayName("Should create category, HTTP status 201")
        @Test
        void shouldCreateCategory() throws Exception {
            CreateCategoryDTO createDTO = new CreateCategoryDTO(NEW_TITLE_FOR_CREATE, DESCRIPTION);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(post(CREATE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(201))
                    .andExpect(header().string(LOCATION, NEW_CATEGORY_LOCATION));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when title is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenTitleIsEmpty() throws Exception {
            CreateCategoryDTO createDTO = new CreateCategoryDTO(EMPTY, DESCRIPTION);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(post(CREATE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when description is too long, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenDescriptionIsTooLong() throws Exception {
            CreateCategoryDTO createDTO = new CreateCategoryDTO(NEW_TITLE, DESCRIPTION_TOO_LONG);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(post(CREATE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should throw CategoryAlreadyExistException when category with given title already exists, HTTP status 400")
        @Test
        void shouldThrowCategoryAlreadyExistException_WhenCategoryWithGivenTitleAlreadyExists() throws Exception {
            CreateCategoryDTO createDTO = new CreateCategoryDTO(TITLE_1, DESCRIPTION);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(post(CREATE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryAlreadyExistException));
        }

        @DisplayName("Should not create category when requested by user, HTTP status 403")
        @Test
        void shouldNotCreateCategory_WhenRequestedByUser() throws Exception {
            CreateCategoryDTO createDTO = new CreateCategoryDTO(NEW_TITLE, DESCRIPTION);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(post(CREATE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not create category when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotCreateCategory_WhenJWTIsWrong() throws Exception {
            CreateCategoryDTO createDTO = new CreateCategoryDTO(NEW_TITLE, DESCRIPTION);
            String createJson = objectMapper.writeValueAsString(createDTO);

            mockMvc.perform(post(CREATE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /categories/admin/edit/title/{categoryId} endpoint")
    @Nested
    class CategoriesAdminEditTitleCategoryIdTest {

        @DisplayName("Should change title")
        @Test
        void shouldChangeTitle() throws Exception {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(TITLE_TO_CHANGE);
            String titleJson = objectMapper.writeValueAsString(titleDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_TITLE_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(TITLE_JSON_PATH).value(TITLE_TO_CHANGE));
        }

        @DisplayName("Should throw CategoryTitleException when given title is the same as title by given id, HTTP status 400")
        @Test
        void shouldThrowCategoryTitleException_WhenGivenTitleIsTheSameAsTitleByGivenId() throws Exception {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(TITLE_1);
            String titleJson = objectMapper.writeValueAsString(titleDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_TITLE_ARE_THE_SAME_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryTitleException));
        }

        @DisplayName("Should throw CategoryDoesntExistException when category doesn't exist by id, HTTP status 404")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenCategoryDoesntExistById() throws Exception {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(NEW_TITLE);
            String titleJson = objectMapper.writeValueAsString(titleDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_TITLE_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesntExistException));
        }

        @DisplayName("Should throw CategoryTitleException when category with given title already exists, HTTP status 400")
        @Test
        void shouldThrowCategoryTitleException_WhenCategoryWithGivenTitleAlreadyExists() throws Exception {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(TITLE_1);
            String titleJson = objectMapper.writeValueAsString(titleDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_TITLE_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryTitleException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when title is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenTitleIsEmpty() throws Exception {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(EMPTY);
            String titleJson = objectMapper.writeValueAsString(titleDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_TITLE_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not change title when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotChangeTitle_WhenJWTIsWrong() throws Exception {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(NEW_TITLE);
            String titleJson = objectMapper.writeValueAsString(titleDTO);

            mockMvc.perform(patch(CHANGE_TITLE_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not change title when requested by moderator, HTTP status 403")
        @Test
        void shouldNotChangeTitle_WhenRequestedByModerator() throws Exception {
            UpdateTopicCategoryDTO titleDTO = new UpdateTopicCategoryDTO(NEW_TITLE);
            String titleJson = objectMapper.writeValueAsString(titleDTO);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_TITLE_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(403));
        }
    }

    @DisplayName("get /categories/admin/edit/description/{categoryId} endpoint")
    @Nested
    class CategoriesAdminEditDescriptionCategoryIdTests {

        @DisplayName("Should change description")
        @Test
        void shouldChangeDescription() throws Exception {
            NewCategoryDescriptionDTO descriptionDTO = new NewCategoryDescriptionDTO(NEW_DESCRIPTION);
            String descriptionJson = objectMapper.writeValueAsString(descriptionDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_DESCRIPTION_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(descriptionJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(DESCRIPTION_JSON_PATH).value(NEW_DESCRIPTION));
        }

        @DisplayName("Should not change title when requested by moderator, HTTP status 403")
        @Test
        void shouldNotChangeTitle_WhenRequestedByModerator() throws Exception {
            NewCategoryDescriptionDTO descriptionDTO = new NewCategoryDescriptionDTO(DESCRIPTION);
            String descriptionJson = objectMapper.writeValueAsString(descriptionDTO);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_DESCRIPTION_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(descriptionJson))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not change title when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotChangeTitle_WhenJWTIsWrong() throws Exception {
            NewCategoryDescriptionDTO descriptionDTO = new NewCategoryDescriptionDTO(NEW_DESCRIPTION);
            String descriptionJson = objectMapper.writeValueAsString(descriptionDTO);

            mockMvc.perform(patch(CHANGE_DESCRIPTION_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(descriptionJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when description is blank, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenDescriptionIsBlank() throws Exception {
            NewCategoryDescriptionDTO descriptionDTO = new NewCategoryDescriptionDTO(EMPTY);
            String descriptionJson = objectMapper.writeValueAsString(descriptionDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_DESCRIPTION_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(descriptionJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should throw CategoryDoesntExistException when category with given id doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenCategoryWithGivenIdDoesntExist() throws Exception {
            NewCategoryDescriptionDTO descriptionDTO = new NewCategoryDescriptionDTO(DESCRIPTION);
            String descriptionJson = objectMapper.writeValueAsString(descriptionDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_DESCRIPTION_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(descriptionJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesntExistException));
        }

        @DisplayName("Should throw CategoryDescriptionException when descriptions are the same, HTTP status 400")
        @Test
        void shouldThrowCategoryDescriptionException_WhenDescriptionAreTheSame() throws Exception {
            NewCategoryDescriptionDTO descriptionDTO = new NewCategoryDescriptionDTO(DESCRIPTION);
            String descriptionJson = objectMapper.writeValueAsString(descriptionDTO);
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(CHANGE_DESCRIPTION_ARE_THE_SAME_ENDPOINT)
                            .header(AUTHORIZATION, adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(descriptionJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDescriptionException));

        }
    }

    @DisplayName("patch /categories/admin/delete/{categoryId} endpoint")
    @Nested
    class CategoriesAdminDeleteCategoryIdTests {

        @DisplayName("Should delete category")
        @Test
        void shouldDeleteCategory() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(delete(DELETE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should not delete category when requested by moderator, HTTP status 403")
        @Test
        void shouldNotDeleteCategory_WhenRequestedByModerator() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(delete(DELETE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not change title when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotChangeTitle_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_CATEGORY_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should throw CategoryDoesntExistException when category with given id doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenCategoryWithGivenIdDoesntExist() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);

            mockMvc.perform(delete(DELETE_CATEGORY_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesntExistException));
        }
    }
}