package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.report.Report;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    @ManyToOne()
    private Topic topic;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Like> likes;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Report> reports;
}