package huberts.spring.forumapp.warning;

import huberts.spring.forumapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarningRepository extends JpaRepository<Warning, Long> {
    void deleteWarningsByUser(User user);
}