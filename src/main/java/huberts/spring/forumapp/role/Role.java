package huberts.spring.forumapp.role;

import huberts.spring.forumapp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "`role`")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
