package huberts.spring.forumapp.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByTitle (String title);
    boolean existsByTitle (String title);
}
