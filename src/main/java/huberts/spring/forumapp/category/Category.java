package huberts.spring.forumapp.category;

import huberts.spring.forumapp.topic.Topic;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column(unique = true)
    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 200)
    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private List<Topic> topics;
}