package huberts.spring.forumapp.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.user.User;
import huberts.spring.forumapp.user.UserRepository;
import huberts.spring.forumapp.user.dto.LoginDTO;
import huberts.spring.forumapp.user.dto.RegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
class SecurityControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("user").password("password").build();
        userRepository.save(user);
    }


    @DisplayName("Login is success, HTTP status 200")
    @Test
    void shouldLoginAndGetContent() throws Exception {
        // given
        LoginDTO login = new LoginDTO("user", "password");

        // when & then
        // Request to the login endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON.getType())
                        .content(toJson(login)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", Matchers.notNullValue()));
    }

    @DisplayName("Login is failed, HTTP status 401")
    @Test
    void shouldNotLogin() throws Exception {
        // given
        LoginDTO login = new LoginDTO("failed", "failed");

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON.getType())
                        .content(toJson(login)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @DisplayName("Creating new user is success, HTTP status 201")
    @Test
    void shouldCreateNewUser() throws Exception {
        // given
        RegisterDTO credentials = new RegisterDTO("credentials", "credentials");

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(credentials)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", is("credentials")))
                .andExpect(MockMvcResultMatchers.header().string("location", "/credentials"));
    }

    @DisplayName("Creating new user is failed, HTTP status 400")
    @Test
    void shouldNotCreateNewUser() throws Exception {
        // given
        RegisterDTO credentials = new RegisterDTO("", "");

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(credentials)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private String toJson(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}