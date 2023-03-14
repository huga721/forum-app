package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByUserAndId(User user, Long id);
    List<Comment> findAllByTopic(Topic topic);
    List<Comment> findAllByUser(User user);
}