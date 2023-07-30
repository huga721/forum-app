package huberts.spring.forumapp.warning;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.exception.user.UserBlockException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.exception.warning.WarningDoesntExistException;
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
class WarningControllerTest extends ContainerIT {

    private static final String WARNED_USER = "userToWarn";
    private static final String WARNED_BANNED_USER = "userToWarnBlock";
    private final static String NOT_BANNED_MESSAGE = "Not banned";
    private final static String BANNED_MESSAGE = "Banned";

    private static final String GET_ALL_WARNINGS_ENDPOINT = "/api/v1/warnings";
    private static final String GET_WARNING_BY_ID_ENDPOINT = "/api/v1/warnings/1";
    private static final String GET_WARNING_BY_ID_DOESNT_EXIST_ENDPOINT = "/api/v1/warnings/999";
    private static final String CREATE_WARNING_ENDPOINT = "/api/v1/warnings/15";
    private static final String CREATE_WARNING_USER_BLOCKED_ENDPOINT = "/api/v1/warnings/14";
    private static final String CREATE_WARNING_5_WARN_ENDPOINT = "/api/v1/warnings/16";
    private static final String CREATE_WARNING_DOESNT_EXIST_ENDPOINT = "/api/v1/warnings/999";
    private static final String DELETE_WARNING_ENDPOINT = "/api/v1/warnings/15";
    private static final String DELETE_WARNING_USER_BLOCKED_ENDPOINT = "/api/v1/warnings/14";
    private static final String DELETE_WARNING_NO_WARNINGS_ENDPOINT = "/api/v1/warnings/4";
    private static final String DELETE_WARNING_DOESNT_EXIST_ENDPOINT = "/api/v1/warnings/999";

    private static final String AUTHORIZATION = "Authorization";
    private static final String INVALID_TOKEN = "wrong_token_123";
    private static final String LOCATION = "location";
    private static final String NEW_WARNING_LOCATION = "/warnings";

    private static final String WARNED_USER_ARRAY_0_JSON_PATH = "$.[0].username";
    private static final String WARNED_USER_ARRAY_1_JSON_PATH = "$.[1].username";
    private static final String WARNED_USER_JSON_PATH = "$.username";
    private static final String WARNING_STATUS_JSON_PATH = "$.status";
    private static final String ID_JSON_PATH = "$.id";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("get /warnings endpoint")
    @Nested
    class WarningsTests {

        @DisplayName("Should return all warnings, HTTP status 200")
        @Test
        void shouldReturnAllWarnings() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_ALL_WARNINGS_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(WARNED_USER_ARRAY_0_JSON_PATH).value(WARNED_USER))
                    .andExpect(jsonPath(WARNED_USER_ARRAY_1_JSON_PATH).value(WARNED_USER));
        }

        @DisplayName("Should not return all warnings when requested by user, HTTP status 403")
        @Test
        void shouldNotReturnAllWarnings_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_ALL_WARNINGS_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not return all warnings when JWT is invalid, HTTP status 401")
        @Test
        void shouldNotReturnAllWarnings_WhenJWTIsInvalid() throws Exception {
            mockMvc.perform(get(GET_ALL_WARNINGS_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not return all warnings when no authorization, HTTP status 401")
        @Test
        void shouldNotReturnAllWarnings_WhenNoAuthorization() throws Exception {
            mockMvc.perform(get(GET_ALL_WARNINGS_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("get /warnings/{warningId} endpoint")
    @Nested
    class WarningsWarningIdTests {

        @DisplayName("Should return warning, HTTP status 200")
        @Test
        void shouldReturnWarning() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_WARNING_BY_ID_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(200))
                    .andExpect(jsonPath(ID_JSON_PATH).value(1))
                    .andExpect(jsonPath(WARNED_USER_JSON_PATH).value(WARNED_USER));
        }

        @DisplayName("Should throw WarningDoesntExistException when warning doesn't exist, HTTP status 404")
        @Test
        void shouldThrowWarningDoesntExistException_WhenWarningDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_WARNING_BY_ID_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof WarningDoesntExistException));
        }

        @DisplayName("Should not return warning when requested by user, HTTP status 403")
        @Test
        void shouldNotReturnWarning_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(get(GET_WARNING_BY_ID_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not return warning when JWT is invalid, HTTP status 401")
        @Test
        void shouldNotReturnWarning_WhenJWTIsInvalid() throws Exception {
            mockMvc.perform(get(GET_WARNING_BY_ID_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not return warning when no authorization, HTTP status 401")
        @Test
        void shouldNotReturnWarning_WhenNoAuthorization() throws Exception {
            mockMvc.perform(get(GET_WARNING_BY_ID_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("post /warnings/{username} endpoint")
    @Nested
    class PostWarningsUsernameTests {

        @DisplayName("Should create warning, HTTP status 201")
        @Test
        void shouldCreateWarning() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_WARNING_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(WARNING_STATUS_JSON_PATH).value(NOT_BANNED_MESSAGE))
                    .andExpect(jsonPath(WARNED_USER_JSON_PATH).value(WARNED_USER))
                    .andExpect(header().string(LOCATION, NEW_WARNING_LOCATION));
        }

        @DisplayName("Should throw UserBlockException when user to warn is already banned, HTTP status 401")
        @Test
        void shouldThrowUserBlockException_WhenUserToWarnIsAlreadyBanned() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_WARNING_USER_BLOCKED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserBlockException));
        }

        @DisplayName("Should create 5th warn then block user, HTTP status 201")
        @Test
        void shouldCreate5thWarn_ThenBlockUser() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_WARNING_5_WARN_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(201))
                    .andExpect(jsonPath(WARNING_STATUS_JSON_PATH).value(BANNED_MESSAGE))
                    .andExpect(jsonPath(WARNED_USER_JSON_PATH).value(WARNED_BANNED_USER))
                    .andExpect(header().string(LOCATION, NEW_WARNING_LOCATION));
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist, HTTP status 404")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_WARNING_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));

        }

        @DisplayName("Should not create warning when requested by user, HTTP status 401")
        @Test
        void shouldNotCreateWarning_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(post(CREATE_WARNING_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not create warning when JWT is invalid, HTTP status 401")
        @Test
        void shouldNotCreateWarning_WhenJWTIsInvalid() throws Exception {
            mockMvc.perform(post(CREATE_WARNING_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not create warning when no authorization, HTTP status 401")
        @Test
        void shouldNotCreateWarning_WhenNoAuthorization() throws Exception {
            mockMvc.perform(post(CREATE_WARNING_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }

    @DisplayName("delete /warnings/{username} endpoint")
    @Nested
    class DeleteWarningsUsernameTests {

        @DisplayName("Should delete warning, HTTP status 204")
        @Test
        void shouldDeleteWarning() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_WARNING_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(204));
        }

        @DisplayName("Should throw UserDoesntExistException when user doesn't exist, HTTP status 404")
        @Test
        void shouldThrowUserDoesntExistException_WhenUserDoesntExist() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_WARNING_DOESNT_EXIST_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(404))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
        }

        @DisplayName("Should throw UserBlockException when user to delete warning is blocked, HTTP status 400")
        @Test
        void shouldThrowUserBlockException_WhenUserToDeleteWarningIsBlocked() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_WARNING_USER_BLOCKED_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserBlockException));
        }

        @DisplayName("Should throw UserBlockException when user to delete warning doesn't have any, HTTP status 400")
        @Test
        void shouldThrowUserBlockException_WhenUserToDeleteWarningDoesntHaveAny() throws Exception {
            String moderatorToken = JwtKey.getModeratorJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_WARNING_NO_WARNINGS_ENDPOINT)
                            .header(AUTHORIZATION, moderatorToken))
                    .andExpect(status().is(400))
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserBlockException));
        }

        @DisplayName("Should not delete warning when requested by user, HTTP status 403")
        @Test
        void shouldNotDeleteWarning_WhenRequestedByUser() throws Exception {
            String userToken = JwtKey.getUserJwt(mockMvc, objectMapper);
            mockMvc.perform(delete(DELETE_WARNING_ENDPOINT)
                            .header(AUTHORIZATION, userToken))
                    .andExpect(status().is(403));
        }

        @DisplayName("Should not delete warning when JWT is invalid, HTTP status 401")
        @Test
        void shouldNotDeleteWarning_WhenJWTIsInvalid() throws Exception {
            mockMvc.perform(delete(DELETE_WARNING_ENDPOINT)
                            .header(AUTHORIZATION, INVALID_TOKEN))
                    .andExpect(status().is(401));
        }

        @DisplayName("Should not delete warning when no authorization, HTTP status 401")
        @Test
        void shouldNotDeleteWarning_WhenNoAuthorization() throws Exception {
            mockMvc.perform(delete(DELETE_WARNING_ENDPOINT))
                    .andExpect(status().is(401));
        }
    }
}