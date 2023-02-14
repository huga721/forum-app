package huberts.spring.forumapp.user;

import huberts.spring.forumapp.post.Post;
import huberts.spring.forumapp.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "`user`")
@Builder
@Data
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
    @JoinColumn(referencedColumnName = "`name`", name = "`role_name`")
    private Role role;
    @OneToMany(mappedBy = "author")
    List<Post> posts = new ArrayList<>();
}