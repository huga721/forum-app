package huberts.spring.forumapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ForumAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumAppApplication.class, args);
    }
}