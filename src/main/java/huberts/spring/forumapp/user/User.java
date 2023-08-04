package huberts.spring.forumapp.user;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.report.Report;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.warning.Warning;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private boolean blocked;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime lastActivity;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Topic> topics;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Report report;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Warning> warnings;

    public void addWarning(Warning warning) {
        warnings.add(warning);
    }
}