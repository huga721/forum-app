package huberts.spring.forumapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


// Class that configures Security, here you put the rules of your security, authorize endpoints, add filters
@EnableWebSecurity
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
                .authorizeHttpRequests(auth -> {
                auth
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/**").permitAll()
                        .anyRequest().authenticated();
                })
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
