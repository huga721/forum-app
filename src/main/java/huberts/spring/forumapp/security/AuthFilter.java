package huberts.spring.forumapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import huberts.spring.forumapp.user.dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper mapper;

    AuthFilter(ObjectMapper mapper, AuthManager manager, AuthSuccessHandler successHandler) {
        this.mapper = mapper;
        this.setAuthenticationManager(manager);
        this.setAuthenticationSuccessHandler(successHandler);
    }

    /**
     * Method that performs authentication, reads data for request, then pass it to the authentication manager and
     * authenticate it (search for the user in database)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Reading the requested credentials.
            LoginDTO login = mapper.readValue(request.getInputStream(), LoginDTO.class);

            log.info("\"attemptAuthentication\" method \n Login: {} | Password: {}", login.getUsername(), login.getPassword());

            // Creating authentication token based on credentials.
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken
                    (login.getUsername(), login.getPassword());

            setDetails(request, token);

            // Passing token to authentication manager to authenticate it.
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
