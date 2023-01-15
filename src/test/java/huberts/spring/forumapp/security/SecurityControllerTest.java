package huberts.spring.forumapp.security;

import huberts.spring.forumapp.user.dto.RegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerTest {

    private MockMvc mockMvc;

    @Test
    void shouldRegisterUser200ok() throws Exception {
        //  given
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("UserTest");
        registerDTO.setPassword("Test123");
        //  when
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON_VALUE).content())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect()
        //  then
    }


}