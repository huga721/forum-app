package huberts.spring.forumapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.user.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
public class JwtKey {

    public static String getJwtForUser(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        LoginDTO login = LoginDTO.builder().username("user").password("encrypted_password").build();

        String jsonLogin = objectMapper.writeValueAsString(login);
        log.info("Credentials as json value: " + jsonLogin);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLogin))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String token = "Bearer " + objectMapper.readTree(responseBody).get("token").asText();
        return token;
    }
}
