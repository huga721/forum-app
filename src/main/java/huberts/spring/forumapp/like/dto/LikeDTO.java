package huberts.spring.forumapp.like.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeDTO {
    public Long id;
    public String who;
    public String likedObject;
    public Long likedObjectId;
}