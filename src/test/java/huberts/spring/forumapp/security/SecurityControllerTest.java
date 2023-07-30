package huberts.spring.forumapp.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.ContainerIT;
import huberts.spring.forumapp.user.dto.LoginDTO;
import huberts.spring.forumapp.user.dto.CredentialsDTO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc
class SecurityControllerTest extends ContainerIT {

    private static final String USER_JWT = "userJwt";
    private static final String PASSWORD = "encrypted_password";
    private static final String USER_BANNED = "userBanned";
    private static final String NEW_USER = "newUser";
    private static final String EMPTY = "";

    private static final String LOGIN_ENDPOINT = "/api/v1/login";
    private static final String REGISTER_ENDPOINT = "/api/v1/register";
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

    @DisplayName("Should throw AccountBlockedException when user is banned, HTTP status 403")
    @Test
    void shouldThrowAccountBlockedException_WhenUserIsBanned() throws Exception {
        LoginDTO login = new LoginDTO(USER_BANNED, PASSWORD);
        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(401));
    }

    @DisplayName("Should create new user, HTTP status 201")
    @Test
    void shouldCreateNewUser() throws Exception {
        CredentialsDTO credentials = new CredentialsDTO(NEW_USER, PASSWORD);

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(credentials)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(USERNAME_JSON_PATH, is(NEW_USER)))
                .andExpect(header().string(LOCATION, NEW_USER_LOCATION_ENDPOINT));
    }

    @DisplayName("Should not create new user when username is empty, HTTP status 400")
    @Test
    void shouldNotCreateNewUser_WhenUsernameIsEmpty() throws Exception {
        CredentialsDTO credentials = new CredentialsDTO(EMPTY, EMPTY);

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(credentials)))
                .andExpect(status().isBadRequest());
    }

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}