package huberts.spring.forumapp.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.comment.dto.CommentContentDTO;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.user.UserIsNotAuthorException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.jwt.JwtKey;
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
class CommentControllerTest extends ContainerIT {

    private static final String COMMENT_CONTENT = "test content of comment";
    private static final String NEW_COMMENT_CONTENT = "new comment";
    private static final String EMPTY_STRING = "";

    private static final String ALL_COMMENTS_ENDPOINT = "/api/v1/comments";
    private static final String GET_COMMENT_BY_ID_ENDPOINT = "/api/v1/comments/1";
    private static final String GET_COMMENT_BY_ID_DOESNT_EXIST_ENDPOINT = "/api/v1/comments/999";
    private static final String GET_COMMENTS_BY_TOPIC_ID_ENDPOINT = "/api/v1/comments/topic/1";
    private static final String GET_COMMENTS_BY_TOPIC_ID_DOESNT_EXIST_ENDPOINT = "/api/v1/comments/topic/999";
    private static final String GET_COMMENTS_BY_USER_ENDPOINT = "/api/v1/comments/user/userJwt";
    private static final String GET_COMMENTS_BY_USER_WRONG_USERNAME_ENDPOINT = "/api/v1/comments/user/fakeUser";
    private static final String GET_COMMENTS_OF_AUTHENTICATED_USER_ENDPOINT = "/api/v1/comments/user";
    private static final String CREATE_COMMENT_ENDPOINT = "/api/v1/comments/topic/2";
    private static final String CREATE_COMMENT_TOPIC_DOESNT_EXIST_ENDPOINT = "/api/v1/comments/topic/999";
    private static final String CREATE_COMMENT_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/comments/topic/8";
    private static final String DELETE_COMMENT_BY_AUTHOR_ENDPOINT = "/api/v1/comments/delete/3";
    private static final String DELETE_COMMENT_BY_AUTHOR_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/comments/delete/5";
    private static final String DELETE_COMMENT_BY_AUTHOR_USER_IS_NOT_AUTHOR_ENDPOINT = "/api/v1/comments/delete/2";
    private static final String DELETE_COMMENT_BY_AUTHOR_DOESNT_EXIST_ENDPOINT = "/api/v1/comments/delete/999";
    private static final String EDIT_COMMENT_BY_AUTHOR_ENDPOINT = "/api/v1/comments/edit/4";
    private static final String EDIT_COMMENT_BY_AUTHOR_DOESNT_EXIST_ENDPOINT = "/api/v1/comments/edit/999";
    private static final String EDIT_COMMENT_BY_AUTHOR_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/comments/edit/5";
    private static final String DELETE_COMMENT_BY_MODERATOR_ENDPOINT = "/api/v1/comments/moderator/delete/6";
    private static final String DELETE_COMMENT_BY_MODERATOR_DOESNT_EXIST_ENDPOINT = "/api/v1/comments/moderator/delete/999";
    private static final String DELETE_COMMENT_BY_MODERATOR_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/comments/moderator/delete/5";
    private static final String EDIT_COMMENT_BY_MODERATOR_ENDPOINT = "/api/v1/comments/moderator/edit/7";
    private static final String EDIT_COMMENT_BY_MODERATOR_DOESNT_EXIST_ENDPOINT = "/api/v1/comments/moderator/edit/999";
    private static final String EDIT_COMMENT_BY_MODERATOR_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/comments/moderator/edit/5";

    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_TOKEN = "wrong_token_123";
    private static final String LOCATION = "location";
    private static final String NEW_COMMENT_LOCATION = "/comments";

    private static final String ID_ARRAY_0_JSON_PATH = "$.[0].id";
    private static final String ID_ARRAY_1_JSON_PATH = "$.[1].id";
    private static final String CONTENT_ARRAY_0_JSON_PATH = "$.[0].content";
    private static final String CONTENT_ARRAY_1_JSON_PATH = "$.[1].content";
    private static final String CONTENT_JSON_PATH = "$.content";
    private static final String JSON_PATH = "$";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("get /comments endpoint")
    @Nested
    class CommentsTests {

        @DisplayName("Should return all comments, HTTP status 200")
        @Test
        void shouldReturnAllComments() throws Exception {
            mockMvc.perform(get(ALL_COMMENTS_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_ARRAY_0_JSON_PATH).value(1))
                    .andExpect(jsonPath(ID_ARRAY_1_JSON_PATH).value(2));
        }
    }

    @DisplayName("get /comments/{commentId} endpoint")
    @Nested
    class CommentIdTests {

        @DisplayName("Should return comment, HTTP status 200")
        @Test
        void shouldReturnComment() throws Exception {
            mockMvc.perform(get(GET_COMMENT_BY_ID_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CONTENT_JSON_PATH).value(COMMENT_CONTENT));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() throws Exception {
            mockMvc.perform(get(GET_COMMENT_BY_ID_DOESNT_EXIST_ENDPOINT))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));
        }
    }

    @DisplayName("get /comments/topic/{topicId} endpoint")
    @Nested
    class CommentsTopicTopicIdTests {

        @DisplayName("Should return all comments by topic id, HTTP status 200")
        @Test
        void shouldReturnAllCommentsByTopicId() throws Exception {
            mockMvc.perform(get(GET_COMMENTS_BY_TOPIC_ID_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CONTENT_ARRAY_0_JSON_PATH).value(COMMENT_CONTENT))
                    .andExpect(jsonPath(CONTENT_ARRAY_1_JSON_PATH).value(COMMENT_CONTENT));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            mockMvc.perform(get(GET_COMMENTS_BY_TOPIC_ID_DOESNT_EXIST_ENDPOINT))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }
    }

    @DisplayName("get /comments/user/{username} endpoint")
    @Nested
    class CommentsUserUsernameTests {

        @DisplayName("Should return all comments of given user, HTTP status 200")
        @Test
        void shouldReturnAllCommentsOfGivenUser() throws Exception {
            mockMvc.perform(get(GET_COMMENTS_BY_USER_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CONTENT_ARRAY_0_JSON_PATH).value(COMMENT_CONTENT))
                    .andExpect(jsonPath(CONTENT_ARRAY_1_JSON_PATH).value(COMMENT_CONTENT));
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist, HTTP status 404")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() throws Exception {
            mockMvc.perform(get(GET_COMMENTS_BY_USER_WRONG_USERNAME_ENDPOINT))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }
    }

    @DisplayName("get /comments/user endpoint")
    @Nested
    class CommentsUserTests {

        @DisplayName("Should return authenticated user comments, HTTP status 200")
        @Test
        void shouldReturnAuthenticatedUserComments() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_COMMENTS_OF_AUTHENTICATED_USER_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_ARRAY_0_JSON_PATH).value(1))
                    .andExpect(jsonPath(ID_ARRAY_1_JSON_PATH).value(2));
        }

        @DisplayName("Should return empty json when user doesn't have comments, HTTP status 200")
        @Test
        void shouldReturnEmptyJson_WhenUserDoesntHaveComments() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_COMMENTS_OF_AUTHENTICATED_USER_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(JSON_PATH).isEmpty());

        }

        @DisplayName("Should not return comments when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnComments_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(get(GET_COMMENTS_OF_AUTHENTICATED_USER_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not return comments when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateTopic_WhenNoAuthorization() throws Exception {
            mockMvc.perform(get(GET_COMMENTS_OF_AUTHENTICATED_USER_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("post /comments/topic/{topicId} endpoint")
    @Nested
    class PostCommentsTopicTopicIdTests {

        @DisplayName("Should create comment, HTTP status 201")
        @Test
        void shouldCreateComment() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(CONTENT_JSON_PATH).value(NEW_COMMENT_CONTENT))
                    .andExpect(header().string(LOCATION, NEW_COMMENT_LOCATION));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_TOPIC_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicIsClosed() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));

        }

        @DisplayName("Should throw MethodArgumentNotValidException when content in request body is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenContentInRequestBodyIsEmpty() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(EMPTY_STRING);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not create comment when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnComments_WhenJWTIsWrong() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            mockMvc.perform(post(CREATE_COMMENT_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not create comment when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateComment_WhenNoAuthorization() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            mockMvc.perform(post(CREATE_COMMENT_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /comments/delete/{commentId} endpoint")
    @Nested
    class CommentsDeleteCommentIdTests {

        @DisplayName("Should delete comment")
        @Test
        void shouldDeleteComment() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to delete is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToDeleteIsClosed() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_AUTHOR_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw UserIsNotAuthorOfComment when comment exist but user is not author, HTTP status 400")
        @Test
        void shouldThrowUserIsNotAuthorOfComment_WhenCommentExistButUserIsNotAuthor() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_AUTHOR_USER_IS_NOT_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIsNotAuthorException));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_AUTHOR_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));
        }

        @DisplayName("Should not delete comment when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteComment_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_COMMENT_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete comment when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteComment_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_COMMENT_BY_AUTHOR_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /comments/edit/{commentId} endpoint")
    @Nested
    class CommentsEditCommentIdTests {

        @DisplayName("Should edit comment")
        @Test
        void shouldEditComment() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CONTENT_JSON_PATH).value(NEW_COMMENT_CONTENT));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_AUTHOR_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));
        }

        @DisplayName("Should throw UserIsNotAuthorException when authenticated user is not author of comment, HTTP status 400")
        @Test
        void shouldThrowUserIsNotAuthorException_WhenAuthenticatedUserIsNotAuthorOfComment() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIsNotAuthorException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where comment is it is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereCommentIsItIsClosed() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_AUTHOR_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when content in request body is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenContentInRequestBodyIsEmpty() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(EMPTY_STRING);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not edit comment when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotEditComment_WhenJWTIsWrong() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);

            mockMvc.perform(patch(EDIT_COMMENT_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not edit comment when no authorization, HTTP status 401")
        @Test
        void shouldNotEditComment_WhenNoAuthorization() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);

            mockMvc.perform(patch(EDIT_COMMENT_BY_AUTHOR_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /comments/moderator/delete/{commentId} endpoint")
    @Nested
    class CommentsModeratorDeleteCommentIdTests {

        @DisplayName("Should delete comment")
        @Test
        void shouldDeleteComment() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_MODERATOR_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to edit is closed , HTTP status 404")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToEditIsClosed() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_MODERATOR_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }


        @DisplayName("Should not delete comment when requested by user, HTTP status 403")
        @Test
        void shouldNotDeleteComment_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_COMMENT_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not delete comment when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteComment_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_COMMENT_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete comment when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteComment_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_COMMENT_BY_MODERATOR_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("patch /comments/moderator/edit/{commentId} endpoint")
    @Nested
    class CommentsModeratorEditCommentIdTests {

        @DisplayName("Should edit comment")
        @Test
        void shouldEditComment() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(CONTENT_JSON_PATH).value(NEW_COMMENT_CONTENT));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(patch(EDIT_COMMENT_BY_MODERATOR_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to edit is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToEditIsClosed() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_MODERATOR_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw MethodArgumentNotValidException when content in request body is empty, HTTP status 400")
        @Test
        void shouldThrowMethodArgumentNotValidException_WhenContentInRequestBodyIsEmpty() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(EMPTY_STRING);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        }

        @DisplayName("Should not edit comment when requested by user, HTTP status 403")
        @Test
        void shouldNotEditComment_WhenRequestedByUser() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);

            mockMvc.perform(patch(EDIT_COMMENT_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not edit comment when JWT is wrong, HTTP status 400")
        @Test
        void shouldNotEditComment_WhenJWTIsWrong() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(EMPTY_STRING);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);

            mockMvc.perform(patch(EDIT_COMMENT_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(400));
        }

        @DisplayName("Should not edit comment when no authorization, HTTP status 401")
        @Test
        void shouldNotEditComment_WhenNoAuthorization() throws Exception {
            CommentContentDTO commentContent = new CommentContentDTO(NEW_COMMENT_CONTENT);
            String commentContentJson = objectMapper.writeValueAsString(commentContent);

            mockMvc.perform(patch(EDIT_COMMENT_BY_MODERATOR_ENDPOINT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(commentContentJson))
                    .andExpect(status().is(401));
        }
    }
}