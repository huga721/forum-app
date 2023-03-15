package huberts.spring.forumapp.like;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndId(User user, Long id);
    boolean existsByTopicAndUser(Topic topic, User user);
    boolean existsByCommentAndUser(Comment comment, User user);
    List<Like> findAllByUser(User user);
}