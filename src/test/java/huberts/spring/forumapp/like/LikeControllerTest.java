package huberts.spring.forumapp.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.exception.comment.CommentDoesntExistException;
import huberts.spring.forumapp.exception.like.LikeAlreadyExistException;
import huberts.spring.forumapp.exception.like.LikeDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicDoesntExistException;
import huberts.spring.forumapp.exception.topic.TopicIsClosedException;
import huberts.spring.forumapp.exception.user.UserIsNotAuthorException;
import huberts.spring.forumapp.jwt.JwtKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class LikeControllerTest extends ContainerIT {

    private final static String STRING_TOPIC = "Topic";
    private final static String STRING_COMMENT = "Comment";

    private static final String GET_ALL_LIKES_ENDPOINT = "/api/v1/likes";
    private static final String GET_LIKE_BY_ID_ENDPOINT = "/api/v1/likes/1";
    private static final String GET_LIKE_BY_ID_DOESNT_EXIST_ENDPOINT = "/api/v1/likes/999";
    private static final String GET_LIKES_BY_USER_ENDPOINT = "/api/v1/likes/user";
    private static final String CREATE_TOPIC_LIKE_ENDPOINT = "/api/v1/likes/topic/2";
    private static final String CREATE_TOPIC_LIKE_DOESNT_EXIST_ENDPOINT = "/api/v1/likes/topic/999";
    private static final String CREATE_TOPIC_LIKE_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/likes/topic/8";
    private static final String CREATE_TOPIC_LIKE_EXISTS_ENDPOINT = "/api/v1/likes/topic/1";
    private static final String CREATE_COMMENT_LIKE_ENDPOINT = "/api/v1/likes/comment/2";
    private static final String CREATE_COMMENT_LIKE_DOESNT_EXIST_ENDPOINT = "/api/v1/likes/comment/999";
    private static final String CREATE_COMMENT_LIKE_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/likes/comment/5";
    private static final String CREATE_COMMENT_LIKE_EXISTS_ENDPOINT = "/api/v1/likes/comment/1";
    private static final String DELETE_LIKE_BY_AUTHOR_ENDPOINT = "/api/v1/likes/delete/3";
    private static final String DELETE_LIKE_BY_AUTHOR_USER_IS_NOT_AUTHOR_ENDPOINT = "/api/v1/likes/delete/4";
    private static final String DELETE_LIKE_BY_AUTHOR_DOESNT_EXIST_ENDPOINT = "/api/v1/likes/delete/999";
    private static final String DELETE_LIKE_BY_AUTHOR_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/likes/delete/5";
    private static final String DELETE_LIKE_BY_MODERATOR_ENDPOINT = "/api/v1/likes/moderator/delete/6";
    private static final String DELETE_LIKE_BY_MODERATOR_DOESNT_EXIST_ENDPOINT = "/api/v1/likes/moderator/delete/999";
    private static final String DELETE_LIKE_BY_MODERATOR_TOPIC_IS_CLOSED_ENDPOINT = "/api/v1/likes/moderator/delete/5";

    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_TOKEN = "wrong_token_123";
    private static final String LOCATION = "location";
    private static final String NEW_LIKE_LOCATION = "/likes";

    private static final String LIKED_OBJECT_ARRAY_0_JSON_PATH = "$.[0].likedObject";
    private static final String LIKED_OBJECT_ARRAY_1_JSON_PATH = "$.[1].likedObject";
    private static final String LIKED_OBJECT_JSON_PATH = "$.likedObject";
    private static final String JSON_PATH = "$";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("get /likes endpoint")
    @Nested
    class LikesTests {

        @DisplayName("Should return all likes, HTTP status 200")
        @Test
        void shouldReturnAllLikes() throws Exception {
            mockMvc.perform(get(GET_ALL_LIKES_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(LIKED_OBJECT_ARRAY_0_JSON_PATH).value(STRING_COMMENT))
                    .andExpect(jsonPath(LIKED_OBJECT_ARRAY_1_JSON_PATH).value(STRING_TOPIC));
        }
    }

    @DisplayName("get /likes/{likeId} endpoint")
    @Nested
    class LikesLikeIdTests {

        @DisplayName("Should return like, HTTP status 200")
        @Test
        void shouldReturnLike() throws Exception {
            mockMvc.perform(get(GET_LIKE_BY_ID_ENDPOINT))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(LIKED_OBJECT_JSON_PATH).value(STRING_COMMENT));
        }

        @DisplayName("Should throw LikeDoesntExistException when like doesn't exist")
        @Test
        void shouldThrowLikeDoesntExistException_WhenLikeDoesntExist() throws Exception {
            mockMvc.perform(get(GET_LIKE_BY_ID_DOESNT_EXIST_ENDPOINT))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof LikeDoesntExistException));
        }
    }

    @DisplayName("get /likes/user endpoint")
    @Nested
    class LikesUserTests {

        @DisplayName("Should return authenticated user likes, HTTP status 200")
        @Test
        void shouldReturnAuthenticatedUserLikes() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_LIKES_BY_USER_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(LIKED_OBJECT_ARRAY_0_JSON_PATH).value(STRING_COMMENT))
                    .andExpect(jsonPath(LIKED_OBJECT_ARRAY_1_JSON_PATH).value(STRING_TOPIC));
        }

        @DisplayName("Should return empty json when user doesn't have likes, HTTP status 200")
        @Test
        void shouldReturnEmptyJson_WhenUserDoesntHaveLikes() throws Exception {
            String adminToken = JwtKey.getAdminJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_LIKES_BY_USER_ENDPOINT)
                            .header(AUTHORIZATION, adminToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(JSON_PATH).isEmpty());
        }

        @DisplayName("Should not return likes when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotReturnLikes_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(get(GET_LIKES_BY_USER_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not return comments when no authorization, HTTP status 401")
        @Test
        void shouldNotReturnComments_WhenNoAuthorization() throws Exception {
            mockMvc.perform(get(GET_LIKES_BY_USER_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("post /likes/topic/{topicId} endpoint")
    @Nested
    class LikesTopicTopicIdTests {

        @DisplayName("Should create like for topic, HTTP status 201")
        @Test
        void shouldCreateLikeForTopic() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_LIKE_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(LIKED_OBJECT_JSON_PATH).value(STRING_TOPIC))
                    .andExpect(header().string(LOCATION, NEW_LIKE_LOCATION));
        }

        @DisplayName("Should throw TopicDoesntExistException when topic doesn't exist, HTTP status 404")
        @Test
        void shouldThrowTopicDoesntExistException_WhenTopicDoesntExist() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_LIKE_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic to like is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicToLikeIsClosed() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_LIKE_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw LikeAlreadyExistException when user already placed like in given topic")
        @Test
        void shouldThrowLikeAlreadyExistException_WhenUserAlreadyPlacedLikeInGivenTopic() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_TOPIC_LIKE_EXISTS_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof LikeAlreadyExistException));
        }

        @DisplayName("Should not create like when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotCreateLike_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(post(CREATE_TOPIC_LIKE_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not create like when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateLike_WhenNotAuthorization() throws Exception {
            mockMvc.perform(post(CREATE_TOPIC_LIKE_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("post /likes/comment/{commentId} endpoint")
    @Nested
    class LikesCommentCommentIdTests {

        @DisplayName("Should create like for comment, HTTP status 201")
        @Test
        void shouldCreateLikeForComment() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_LIKE_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(LIKED_OBJECT_JSON_PATH).value(STRING_COMMENT))
                    .andExpect(header().string(LOCATION, NEW_LIKE_LOCATION));
        }

        @DisplayName("Should throw CommentDoesntExistException when comment doesn't exist, HTTP status 404")
        @Test
        void shouldThrowCommentDoesntExistException_WhenCommentDoesntExist() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_LIKE_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof CommentDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where comment is placed it is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereCommentIsPlacedItIsClosed() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_LIKE_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should throw LikeAlreadyExistException when user already placed like in comment, HTTP status 400")
        @Test
        void shouldThrowLikeAlreadyExistException_WhenUserAlreadyPlacedLikeInComment() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_COMMENT_LIKE_EXISTS_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof LikeAlreadyExistException));
        }

        @DisplayName("Should not create like when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotCreateLike_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(post(CREATE_COMMENT_LIKE_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not create like when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateLike_WhenNotAuthorization() throws Exception {
            mockMvc.perform(post(CREATE_COMMENT_LIKE_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /likes/delete/{commentId} endpoint")
    @Nested
    class LikesDeleteCommentIdTests {

        @DisplayName("Should delete comment by author, HTTP status 204")
        @Test
        void shouldDeleteCommentByAuthor() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw LikeDoesntExistException when like doesn't exist, HTTP status 404")
        @Test
        void shouldThrowLikeDoesntExistException_WhenLikeDoesntExist() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_AUTHOR_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof LikeDoesntExistException));
        }

        @DisplayName("Should throw UserIsNotAuthorException when like exist but user is not author, HTTP status 400")
        @Test
        void shouldThrowUserIsNotAuthorException_WhenLikeExistButUserIsNotAuthor() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_AUTHOR_USER_IS_NOT_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserIsNotAuthorException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to delete is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToDeleteIsClosed() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_AUTHOR_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should not delete like when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteLike_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_LIKE_BY_AUTHOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete like when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteLike_WhenNotAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_LIKE_BY_AUTHOR_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /likes/moderator/delete/{commentId} endpoint")
    @Nested
    class LikesModeratorDeleteCommentIdTests {

        @DisplayName("Should delete comment by moderator, HTTP status 204")
        @Test
        void shouldDeleteCommentByModerator() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw LikeDoesntExistException when like doesn't exist, HTTP status 404")
        @Test
        void shouldThrowLikeDoesntExistException_WhenLikeDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_MODERATOR_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof LikeDoesntExistException));
        }

        @DisplayName("Should throw TopicIsClosedException when topic where is comment to delete is closed, HTTP status 400")
        @Test
        void shouldThrowTopicIsClosedException_WhenTopicWhereIsCommentToDeleteIsClosed() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_MODERATOR_TOPIC_IS_CLOSED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TopicIsClosedException));
        }

        @DisplayName("Should not delete comment when requested by user, HTTP status 403")
        @Test
        void shouldNotDeleteComment_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_LIKE_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not delete like when JWT is wrong, HTTP status 401")
        @Test
        void shouldNotDeleteLike_WhenJWTIsWrong() throws Exception {
            mockMvc.perform(delete(DELETE_LIKE_BY_MODERATOR_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete like when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteLike_WhenNotAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_LIKE_BY_MODERATOR_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }
}