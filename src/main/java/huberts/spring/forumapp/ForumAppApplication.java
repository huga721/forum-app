package huberts.spring.forumapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForumAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumAppApplication.class, args);
    }

//        @Bean
//        CommandLineRunner commandLineRunner(RoleService roleService) {
//            return args -> {
//                roleService.addRole(new Role(1, "ROLE_USER", "123U"));
//                roleService.addRole(new Role(2, "ROLE_MODERATOR", "456M"));
//                roleService.addRole(new Role(3, "ROLE_ADMIN", "789A"));
//            };
//        }
}
