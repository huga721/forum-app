package huberts.spring.forumapp.like;

import huberts.spring.forumapp.like.dto.LikeDTO;

import java.util.List;

public interface LikeServiceApi {
    LikeDTO createTopicLike(Long id, String username);
    LikeDTO createCommentLike(Long id, String username);

    LikeDTO getLikeById(Long id);
    List<LikeDTO> getAllLikes();
    List<LikeDTO> getAllLikesByUsername(String username);

    void deleteLikeByCurrentUser(Long id, String username);
    void deleteLikeByModerator(Long id);
}