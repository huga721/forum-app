package huberts.spring.forumapp.like;

import huberts.spring.forumapp.like.dto.LikeDTO;

import java.util.List;

public interface LikeServiceApi {
    LikeDTO createTopicLike(Long likeId, String username);
    LikeDTO createCommentLike(Long likeId, String username);

    LikeDTO getLikeById(Long likeId);
    List<LikeDTO> getAllLikes();
    List<LikeDTO> getAllLikesByUsername(String username);

    void deleteLikeByAuthor(Long likeId, String username);
    void deleteLikeByModerator(Long likeId);
}