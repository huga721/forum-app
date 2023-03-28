package huberts.spring.forumapp.like;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "likes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne()
    @JoinColumn(name = "comment_id")
    private Comment comment;
}