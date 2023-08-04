package huberts.spring.forumapp.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.security.AuthManager;
import huberts.spring.forumapp.security.AuthSuccessHandler;
import huberts.spring.forumapp.user.dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper;

    public AuthFilter(ObjectMapper mapper, AuthManager manager, AuthSuccessHandler successHandler) {
        this.mapper = mapper;
        setAuthenticationManager(manager);
        setAuthenticationSuccessHandler(successHandler);
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = request.getInputStream();
            LoginDTO login = mapper.readValue(inputStream, LoginDTO.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    login.username(), login.password());

            setDetails(request, token);

            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}