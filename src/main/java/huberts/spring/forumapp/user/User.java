package huberts.spring.forumapp.user;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.report.Report;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.role.Role;
import huberts.spring.forumapp.warning.Warning;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    private boolean blocked;

    @OneToOne
    @JoinColumn(referencedColumnName = "name", name = "role_name")
    private Role role;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    private List<Topic> topics;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime lastActivity;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Report report;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Warning> warnings;
}