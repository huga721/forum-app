package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.report.Report;
import huberts.spring.forumapp.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity(name = "topic")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    private String title;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime lastEdit;

    private boolean closed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE)
    private List<Report> reports;
}