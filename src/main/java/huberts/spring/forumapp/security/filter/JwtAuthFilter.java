package huberts.spring.forumapp.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import huberts.spring.forumapp.security.DetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Slf4j
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace("Bearer ", ""))
                .getSubject();
        if (username != null) {
            UserDetails userDetails = service.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            String errorMessage = "User not found";
            log.error("Exception occurred while authentication JWT", new UsernameNotFoundException(errorMessage));
            throw new UsernameNotFoundException(errorMessage);
        }
    }
}