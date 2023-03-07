package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTitle(String title);
    Optional<Topic> findByUsersAndId(User user, Long id);
    Optional<Topic> findByTitleAndCategories(String title, Category category);
    boolean existsByTitle(String title);
    boolean existsByTitleAndUsersAndCategories(String title, User user, Category category);
    List<Topic> findAllByTitle(String title);
    Topic findByTitleAndCategoriesAndUsers(String title, Category category, User user);
}
