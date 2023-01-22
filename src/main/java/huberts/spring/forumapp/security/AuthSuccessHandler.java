package huberts.spring.forumapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PropertySource("classpath:/customAppProperties.properties")
@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private int expirationTime;
    private final String secret;

    public AuthSuccessHandler(@Value("${spring.custom.jwt.expiration}") int expirationTime,
                              @Value("${spring.custom.jwt.secret}") String secret) {
        this.expirationTime = expirationTime;
        this.secret = secret;
    }

    /**
     * Creates token on succeed authentication.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        var user = authentication.getPrincipal();

        String token = JWT.create()
                .withSubject(user.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secret));

        Map<String, String> content = new HashMap<>();
        content.put("token", token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), content);
    }
}
