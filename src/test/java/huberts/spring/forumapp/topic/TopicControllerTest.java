package huberts.spring.forumapp.topic;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.category.dto.CategoryTitleDTO;
import huberts.spring.forumapp.exception.category.CategoryDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicAlreadyExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.jwt.JwtKey;
import huberts.spring.forumapp.topic.dto.CloseReasonDTO;
import huberts.spring.forumapp.topic.dto.TopicCreateDTO;
import huberts.spring.forumapp.topic.dto.TopicEditDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class TopicControllerTest extends ContainerIT {

    private static final String TOPIC_TITLE_CREATE = "title of topic to create";
    private static final String TOPIC_TITLE_DUPLICATE = "topic duplicate";
    private static final String TOPIC_CONTENT_CREATE = "content of topic to create";
    private static final String TOPIC_NOT_EDITED_BY_MODERATOR = "topic to edit by moderator";
    private static final String CATEGORY_TITLE = "test title";
    private static final String CATEGORY_NOT_FOUND = "this category doesn't exist";
    private static final String CATEGORY_TITLE_SECOND = "second test title";
    private static final String REASON = "closing this topic because it breaks TOS";
    private static final String TITLE_TO_CHANGE = "changed title";
    private static final String CONTENT_TO_CHANGE = "changed content";
    private static final String CONTENT_DEFAULT = "random test content";
    private static final String EMPTY_STRING = "";

    private static final String ALL_TOPICS_ENDPOINT = "/topics";
    private static final String GET_TOPIC_BY_ID_ENDPOINT = "/topics/1";
    private static final String GET_TOPIC_BY_ID_DOESNT_EXIST_ENDPOINT = "/topics/999";
    private static final String CREATE_TOPIC_ENDPOINT = "/topics/create";
    private static final String DELETE_TOPIC_ENDPOINT = "/topics/delete/4";
    private static final String DELETE_TOPIC_WRONG_AUTHOR_ENDPOINT = "/topics/delete/5";
    private static final String DELETE_TOPIC_ID_DOESNT_EXIST_ENDPOINT = "/topics/delete/999";
    private static final String CLOSE_TOPIC_BY_AUTHOR_ENDPOINT = "/topics/close-topic/6";
    private static final String CLOSE_TOPIC_BY_AUTHOR_WRONG_AUTHOR_ENDPOINT = "/topics/close-topic/1";
    private static final String CLOSE_TOPIC_BY_AUTHOR_ID_DOESNT_EXIST_ENDPOINT = "/topics/close-topic/999";
    private static final String EDIT_TOPIC_BY_AUTHOR_ENDPOINT = "/topics/edit/7";
    private static final String EDIT_TOPIC_BY_AUTHOR_WRONG_AUTHOR_ENDPOINT = "/topics/edit/1";
    private static final String EDIT_TOPIC_BY_AUTHOR_ID_DOESNT_EXIST_ENDPOINT = "/topics/edit/999";
    private static final String EDIT_TOPIC_BY_AUTHOR_IS_CLOSED_ENDPOINT = "/topics/edit/8";
    private static final String EDIT_TOPIC_BY_MODERATOR_ENDPOINT = "/topics/moderator/edit/9";
    private static final String EDIT_TOPIC_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT = "/topics/moderator/edit/999";
    private static final String EDIT_TOPIC_BY_MODERATOR_IS_CLOSED_ENDPOINT = "/topics/moderator/edit/8";
    private static final String CHANGE_CATEGORY_BY_MODERATOR_ENDPOINT = "/topics/moderator/change-category/11";
    private static final String CHANGE_CATEGORY_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT = "/topics/moderator/change-category/999";
    private static final String CHANGE_CATEGORY_BY_MODERATOR_IS_CLOSED_ENDPOINT = "/topics/moderator/change-category/8";
    private static final String CLOSE_TOPIC_BY_MODERATOR_ENDPOINT = "/topics/moderator/close-topic/12";
    private static final String CLOSE_TOPIC_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT = "/topics/moderator/close-topic/999";
    private static final String DELETE_TOPIC_BY_MODERATOR_ENDPOINT = "/topics/moderator/delete/13";
    private static final String DELETE_TOPIC_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT = "/topics/moderator/delete/999";

    private static final String ID_ARRAY_0_JSON_PATH = "$.[0].id";
    private static final String ID_ARRAY_1_JSON_PATH = "$.[1].id";
    private static final String ID_JSON_PATH = "$.id";
    private static final String TITLE_JSON_PATH = "$.title";
    private static final String CONTENT_JSON_PATH = "$.content";
    private static final String CLOSED_JSON_PATH = "$.closed";
    private static final String CATEGORY_JSON_PATH = "$.categoryName";

    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_TOKEN = "wrong_token_123";
    private static final String LOCATION = "location";
    private static final String NEW_TOPIC_LOCATION = "/topics";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("get /topics endpoint")
    @Nested
    class TopicsTests {

        @DisplayName("Should return all topics, HTTP status 200")
        @Test
        void shouldReturnAllTopic() throws Exception {
            mockMvc.perform(get(ALL_TOPICS_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_ARRAY_0_JSON_PATH).value(1))
                    .andExpect(jsonPath(ID_ARRAY_1_JSON_PATH).value(2));
        }
    }

    @DisplayName("get /topics/{topicId} endpoint")
    @Nested
    class TopicsTopicIdTests {

        @DisplayName("Should return topic, HTTP status 200")
        @Test
        void shouldReturnTopic() throws Exception {
            mockMvc.perform(get(GET_TOPIC_BY_ID_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_JSON_PATH).value(1));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            mockMvc.perform(get(GET_TOPIC_BY_ID_DOESNT_EXIST_ENDPOINT))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }
    }

    @DisplayName("post /topics/create endpoint")
    @Nested
    class TopicsCreateTests {

        @DisplayName("Should create topic, HTTP status 201")
        @Test
        void shouldCreateTopic() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_CREATE, TOPIC_CONTENT_CREATE, CATEGORY_TITLE);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(TITLE_JSON_PATH).value(TOPIC_TITLE_CREATE))
                    .andExpect(header().string(LOCATION, NEW_TOPIC_LOCATION));
        }

        @DisplayName("Should create topic with same title but in other category, HTTP status 201")
        @Test
        void shouldCreateTopicWithSameTitleButInOtherCategory() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_CREATE, TOPIC_CONTENT_CREATE, CATEGORY_TITLE_SECOND);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(TITLE_JSON_PATH).value(TOPIC_TITLE_CREATE))
                    .andExpect(header().string(LOCATION, NEW_TOPIC_LOCATION));
        }

        @DisplayName("Should throw TopicAlreadyExistException when creating topic with same title in category , HTTP status 400")
        @Test
        void shouldThrowTopicAlreadyExistException_WhenCreatingTopicWithSameTitleInCategory() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_DUPLICATE, TOPIC_CONTENT_CREATE, CATEGORY_TITLE_SECOND);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicAlreadyExistException));
        }

        @DisplayName("Should throw CategoryDoesntExistException when category to create topic in doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenCategoryToCreateTopicInDoesntExist() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_CREATE, TOPIC_CONTENT_CREATE, CATEGORY_NOT_FOUND);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesntExistException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when title is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenTitleIsEmpty() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(EMPTY_STRING, TOPIC_CONTENT_CREATE, CATEGORY_NOT_FOUND);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when content is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenContentIsEmpty() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_CREATE, EMPTY_STRING, CATEGORY_NOT_FOUND);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when category is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenCategoryIsEmpty() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_CREATE, TOPIC_CONTENT_CREATE, EMPTY_STRING);
            String createJson = objectMapper.writeValueAsString(createDTO);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not create topic when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotCreateTopic_WhenJWTIsWrong() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_CREATE, TOPIC_CONTENT_CREATE, CATEGORY_TITLE);
            String createJson = objectMapper.writeValueAsString(createDTO);
            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not create topic when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateTopic_WhenNoAuthorization() throws Exception {
            TopicCreateDTO createDTO = new TopicCreateDTO(TOPIC_TITLE_CREATE, TOPIC_CONTENT_CREATE, CATEGORY_TITLE);
            String createJson = objectMapper.writeValueAsString(createDTO);
            mockMvc.perform(post(CREATE_TOPIC_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("post /topics/delete/{topicId} endpoint")
    @Nested
    class TopicsDeleteTopicIdTests {

        @DisplayName("Should delete topic, HTTP status 204")
        @Test
        void shouldDeleteTopic() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id and user as author doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdAndUserAsAuthorDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_TOPIC_WRONG_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_TOPIC_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should not delete topic when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteTopic_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_TOPIC_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete topic when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteTopic_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_TOPIC_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }


    @DisplayName("patch /topics/close-topic/topicId endpoint")
    @Nested
    class TopicsCloseTopicTopicIdTests {

        @DisplayName("Should close topic, HTTP status 200")
        @Test
        void shouldCloseTopic() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CLOSED_JSON_PATH).value(true));
        }

        @DisplayName("Should throw TopicDoesntExistException when given user is not the author of topic with given id, HTTP status 400")
        @Test
        void shouldThrowTopicDoesntExistException_WhenGivenUserIsNotTheAuthorOfTopicWithGivenId() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_AUTHOR_WRONG_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given ID doesn't exist, HTTP status 400")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_AUTHOR_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when reason in request body is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenReasonInRequestBodyIsEmpty() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(EMPTY_STRING);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not delete topic when JWT is wrong")
        @Test
        void shouldNotCloseTopic_WhenJWTIsWrong() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete topic when no authorization, HTTP status 401")
        @Test
        void shouldNotCloseTopic_WhenNoAuthorization() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_AUTHOR_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /topics/edit/topicId endpoint")
    @Nested
    class TopicsEditTopicIdTests {

        @DisplayName("Should edit topic")
        @Test
        void shouldEditTopic() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(TITLE_JSON_PATH).value(TITLE_TO_CHANGE))
                    .andExpect(jsonPath(CONTENT_JSON_PATH).value(CONTENT_TO_CHANGE));
        }

        @DisplayName("Should not edit topic with empty request body")
        @Test
        void shouldNotEditTopicWithEmptyRequestBody() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(EMPTY_STRING, EMPTY_STRING);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(200));
        }

        @DisplayName("Should throw TopicDoesntExistException when user is not owner of the topic")
        @Test
        void shouldThrowTopicDoesntExistException_WhenUserIsNotOwnerOfTheTopic() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_AUTHOR_WRONG_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given ID doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicWithGivenIdDoesntExist() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_AUTHOR_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicIsClosed() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_AUTHOR_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should not edit topic when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotEditTopic_WhenJWTIsWrong() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            mockMvc.perform(patch(EDIT_TOPIC_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not edit topic when no authorization, HTTP status 401")
        @Test
        void shouldNotEditTopic_WhenNoAuthorization() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            mockMvc.perform(patch(EDIT_TOPIC_BY_AUTHOR_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /topics/moderator/edit/topicId endpoint")
    @Nested
    class TopicsModeratorEditTopicIdTests {

        @DisplayName("Should edit topic, HTTP status 200")
        @Test
        void shouldEditTopic() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(TITLE_JSON_PATH).value(TITLE_TO_CHANGE))
                    .andExpect(jsonPath(CONTENT_JSON_PATH).value(CONTENT_TO_CHANGE));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicIsClosed() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_MODERATOR_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should not edit topic with empty request body")
        @Test
        void shouldNotEditTopicWithEmptyRequestBody() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(EMPTY_STRING, EMPTY_STRING);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(TITLE_JSON_PATH).value(TOPIC_NOT_EDITED_BY_MODERATOR))
                    .andExpect(jsonPath(CONTENT_JSON_PATH).value(CONTENT_DEFAULT));
        }

        @DisplayName("Should not edit topic when requested by user, HTTP status 403")
        @Test
        void shouldNotEditTopic_WhenRequestedByUser() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not edit topic when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteTopic_WhenJWTIsWrong() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            mockMvc.perform(patch(EDIT_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not edit topic when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteTopic_WhenNoAuthorization() throws Exception {
            TopicEditDTO editBody = new TopicEditDTO(TITLE_TO_CHANGE, CONTENT_TO_CHANGE);
            String editBodyJson = objectMapper.writeValueAsString(editBody);
            mockMvc.perform(patch(EDIT_TOPIC_BY_MODERATOR_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(editBodyJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /topics/moderator/change-category/topicId endpoint")
    @Nested
    class TopicsModeratorChangeCategoryTopicIdTests {

        @DisplayName("Should change category")
        @Test
        void shouldChangeCategory() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(CATEGORY_TITLE_SECOND);
            String titleJson = objectMapper.writeValueAsString(title);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CATEGORY_JSON_PATH).value(CATEGORY_TITLE_SECOND));
        }

        @DisplayName("Should throw TopicIsClosedException when topic to change category is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicToChangeCategoryIsClosed() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(CATEGORY_TITLE_SECOND);
            String titleJson = objectMapper.writeValueAsString(title);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw CategoryDoesntExistException when category to change in topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCategoryDoesntExistException_WhenCategoryToChangeInTopicDoesntExist() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(CATEGORY_NOT_FOUND);
            String titleJson = objectMapper.writeValueAsString(title);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesntExistException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when category title in request body is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenCategoryTitleInRequestBodyIsEmpty() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(EMPTY_STRING);
            String titleJson = objectMapper.writeValueAsString(title);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(CATEGORY_TITLE_SECOND);
            String titleJson = objectMapper.writeValueAsString(title);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should not change category when requested by user, HTTP status 403")
        @Test
        void shouldNotDeleteTopic_WhenRequestByUser() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(CATEGORY_TITLE_SECOND);
            String titleJson = objectMapper.writeValueAsString(title);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not change category when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteTopic_WhenJWTIsWrong() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(CATEGORY_TITLE_SECOND);
            String titleJson = objectMapper.writeValueAsString(title);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not change category when no authorization, HTTP status 401")
        @Test
        void shouldNotChangeCategory_WhenNoAuthorization() throws Exception {
            CategoryTitleDTO title = new CategoryTitleDTO(CATEGORY_TITLE_SECOND);
            String titleJson = objectMapper.writeValueAsString(title);
            mockMvc.perform(patch(CHANGE_CATEGORY_BY_MODERATOR_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /topics/moderator/close-topic/topicId endpoint")
    @Nested
    class TopicsModeratorCloseTopicTopicIdTests {

        @DisplayName("Should close topic, HTTP status 200")
        @Test
        void shouldCloseTopic() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CLOSED_JSON_PATH).value(true));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic with given id doesn't exist, HTTP status 404")
        @Test
        void shouldCloseTopicByModerator() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when reason in request body is empty, HTTP status 200")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenReasonInRequestBodyIsEmpty() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(EMPTY_STRING);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }


        @DisplayName("Should not close topic when requested by user, HTTP status 403")
        @Test
        void shouldNotCloseTopic_WhenRequestByUser() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            String titleJson = objectMapper.writeValueAsString(closeReasonJson);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(titleJson))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not close topic when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotCloseTopic_WhenJWTIsWrong() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not close topic when no authorization, HTTP status 401")
        @Test
        void shouldNotCloseTopic_WhenNoAuthorization() throws Exception {
            CloseReasonDTO closeReason = new CloseReasonDTO(REASON);
            String closeReasonJson = objectMapper.writeValueAsString(closeReason);
            mockMvc.perform(patch(CLOSE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(closeReasonJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /topics/moderator/delete/topicId endpoint")
    @Nested
    class TopicsModeratorDeleteTopicIdTests {

        @DisplayName("Should delete topic, HTTP status 200")
        @Test
        void shouldDeleteTopic() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic to delete doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicToDeleteDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_TOPIC_BY_MODERATOR_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should not delete topic when requested by user, HTTP status 403")
        @Test
        void shouldNotDeleteTopic_WhenRequestByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not delete topic when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteTopic_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_TOPIC_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete topic when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteTopic_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_TOPIC_BY_MODERATOR_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }
}