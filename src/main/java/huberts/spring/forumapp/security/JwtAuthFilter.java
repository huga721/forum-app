package huberts.spring.forumapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthFilter extends BasicAuthenticationFilter {
    private final String secret;
    private final DetailsService service;

    public JwtAuthFilter(AuthenticationManager authenticationManager,
                         @Value("${spring.custom.jwt.secret}") String secret,
                         DetailsService service) {
        super(authenticationManager);
        this.secret = secret;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication = null;

        // Retrieving token from client
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            // Verification of secret, username of client that we gonna get from JWT
            String username = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    // Decoding JWT
                    .verify(token.replace("Bearer ", ""))
                    // Receiving username of client
                    .getSubject();

            // If username is correct
            if (username != null) {
                // Fetch user by username
                UserDetails user = service.loadUserByUsername(username);

                authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                        null, user.getAuthorities());
            } else {
                throw new RuntimeException("Error in JwtAuthFilter");
            }
        }
        if (authentication == null) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
