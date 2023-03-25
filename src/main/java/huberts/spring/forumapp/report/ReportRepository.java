package huberts.spring.forumapp.report;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByTopic(Topic topic);
    List<Report> findAllByComment(Comment comment);
    @Query("SELECT e FROM Report e ORDER BY e.createdAt ASC")
    List<Report> findAllSortedByTopic();
}

