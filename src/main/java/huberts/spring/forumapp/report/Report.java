package huberts.spring.forumapp.report;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDateTime createdAt;
}