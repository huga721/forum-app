package huberts.spring.forumapp.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.exception.user.AccountBlockedException;
import huberts.spring.forumapp.exception.user.UserDoesntExistException;
import huberts.spring.forumapp.user.dto.LoginDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class SecurityControllerTest extends ContainerIT {

    private static final String USER_JWT = "userJwt";
    private static final String PASSWORD = "encrypted_password";
    private static final String USER_BANNED = "userBanned";
    private static final String INVALID = "invalid";
    private static final String NEW_USER = "newUser";
    private static final String EMPTY = "";

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String REGISTER_ENDPOINT = "/register";
    private static final String NEW_USER_LOCATION_ENDPOINT = "/newUser";

    private static final String USERNAME_JSON_PATH = "$.username";
    private static final String TOKEN_JSON_PATH = "$.token";
    private static final String LOCATION = "location";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Should login, HTTP status 200")
    @Test
    void shouldLogin() throws Exception {
        LoginDTO login = new LoginDTO(USER_JWT, PASSWORD);
        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andExpect(jsonPath(TOKEN_JSON_PATH, Matchers.notNullValue()));
    }

    //TODO: Fix it after answers in stackoverflow, if not then only assert HTTP status

    @DisplayName("Should throw BadCredentialsException when password doesn't match, HTTP status 403")
    @Test
    void shouldThrowBadCredentialsException_WhenPasswordDoesntMatch() throws Exception {
        LoginDTO login = new LoginDTO(USER_JWT, PASSWORD);
        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(401))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof BadCredentialsException));
    }

    //TODO: Fix it after answers in stackoverflow, if not then only assert HTTP status

    @DisplayName("Should throw AccountBlockedException when user is banned, HTTP status 403")
    @Test
    void shouldThrowAccountBlockedException_WhenUserIsBanned() throws Exception {
        LoginDTO login = new LoginDTO(USER_BANNED, PASSWORD);
        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(401))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof AccountBlockedException));
    }

    //TODO: Fix it after answers in stackoverflow, if not then only assert HTTP status

    @DisplayName("Should not login when user doesn't exist, HTTP status 401")
    @Test
    void shouldNotLogin() throws Exception {
        LoginDTO login = new LoginDTO(INVALID, PASSWORD);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON.getType())
                        .content(toJson(login)))
                .andExpect(status().is(401))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UserDoesntExistException));
    }

    @DisplayName("Should create new user, HTTP status 201")
    @Test
    void shouldCreateNewUser() throws Exception {
        RegisterDTO credentials = new RegisterDTO(NEW_USER, PASSWORD);

        mockMvc.perform(post(REGISTER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(credentials)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(USERNAME_JSON_PATH, is(NEW_USER)))
                .andExpect(header().string(LOCATION, NEW_USER_LOCATION_ENDPOINT));
    }

    @DisplayName("Creating new user is failed, HTTP status 400")
    @Test
    void shouldNotCreateNewUser() throws Exception {
        // given
        RegisterDTO credentials = new RegisterDTO(EMPTY, EMPTY);

        // when & then
        mockMvc.perform(post(REGISTER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(credentials)))
                .andExpect(status().isBadRequest());
    }

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}