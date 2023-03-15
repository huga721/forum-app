package huberts.spring.forumapp.like.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeListDTO {
    public Long id;
    public String who;
}
