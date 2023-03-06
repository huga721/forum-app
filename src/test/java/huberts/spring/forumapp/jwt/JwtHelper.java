package huberts.spring.forumapp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.user.dto.LoginDTO;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtHelper {

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN = "token";

    static String returnJwt(LoginDTO credentials, ObjectMapper objectMapper, MockMvc mockMvc) throws Exception {
        String jsonCredentials = objectMapper.writeValueAsString(credentials);

        String jsonResult = mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCredentials))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return BEARER_PREFIX + objectMapper.readTree(jsonResult).get(TOKEN).asText();
    }
}