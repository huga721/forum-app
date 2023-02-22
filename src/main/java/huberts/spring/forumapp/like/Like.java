//package huberts.spring.forumapp.like;
//
//import huberts.spring.forumapp.post.Post;
//import huberts.spring.forumapp.user.User;
//import jakarta.persistence.*;
//
//import java.util.Set;
//
//@Entity(name = "`like`")
//public class Like {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
////    @OneToMany
////    private Set<Post> posts;
//}
