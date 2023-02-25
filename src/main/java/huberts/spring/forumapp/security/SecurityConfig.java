package huberts.spring.forumapp.security;

import huberts.spring.forumapp.security.filter.AuthFilter;
import huberts.spring.forumapp.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthFilter authenticationFilter;
    private final AuthManager authenticationManager;
    private final DetailsService detailsService;
    @Value("${spring.custom.jwt.secret}")
    private String secret;


    /**
     * Method to configure your security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                .and()
                .addFilter(authenticationFilter)
                .addFilter(new JwtAuthFilter(authenticationManager, secret, detailsService))
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .build();

    }
}
