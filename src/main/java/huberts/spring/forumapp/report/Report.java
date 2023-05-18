package huberts.spring.forumapp.report;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;

    private boolean seen;

    @OneToOne
    @JoinColumn(referencedColumnName = "username", name = "user_report")
    private User user;

    @ManyToOne
    private Topic topic;

    @ManyToOne
    private Comment comment;

    @CreatedDate
    private LocalDateTime createdAt;
}