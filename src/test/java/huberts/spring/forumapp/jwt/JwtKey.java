package huberts.spring.forumapp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.user.dto.LoginDTO;
import org.springframework.test.web.servlet.MockMvc;

public class JwtKey {

    private static final String USER_USERNAME = "userJwt";
    private static final String USER_TO_DELETE_USERNAME = "userToDelete";
    private static final String MODERATOR_USERNAME = "moderatorJwt";
    private static final String ADMIN_USERNAME = "adminJwt";
    private static final String PASSWORD = "encrypted_password";

    public static String getUserJwt(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        return getJwt(mockMvc, objectMapper, USER_USERNAME);
    }

    public static String getUserToDeleteJwt(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        return getJwt(mockMvc, objectMapper, USER_TO_DELETE_USERNAME);
    }

    public static String getModeratorJwt(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        return getJwt(mockMvc, objectMapper, MODERATOR_USERNAME);
    }

    public static String getAdminJwt(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        return getJwt(mockMvc, objectMapper, ADMIN_USERNAME);
    }

    private static String getJwt(MockMvc mockMvc, ObjectMapper objectMapper, String username) throws Exception {
        LoginDTO credentials = LoginDTO.builder()
                .username(username)
                .password(PASSWORD)
                .build();
        return JwtHelper.returnJwt(credentials, objectMapper, mockMvc);
    }
}