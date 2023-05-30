package huberts.spring.forumapp.like;

import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.like.dto.LikeListDTO;
import huberts.spring.forumapp.like.dto.LikeDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LikeMapper {

    public static Like buildTopicLike(User user, Topic topic) {
        return Like.builder()
                .user(user)
                .topic(topic)
                .build();
    }

    public static Like buildCommentLike(User user, Comment comment) {
        return Like.builder()
                .user(user)
                .comment(comment)
                .build();
    }

    public static LikeDTO buildLikeDTO(Like like) {
        String username = like.getUser().getUsername();
        return LikeDTO.builder()
                .id(like.getId())
                .who(username)
                .likedObjectId(getLikedObjectId(like))
                .likedObject(getLikedObject(like))
                .build();
    }

    private static Long getLikedObjectId(Like like) {
        if (like.getTopic() != null)
            return like.getTopic().getId();
        else
            return like.getComment().getId();
    }

    private static String getLikedObject(Like like) {
        if (like.getTopic() != null)
            return "Topic";
        else
            return "Comment";
    }

    public static LikeListDTO buildLikeListDTO(Like like) {
        String username = like.getUser().getUsername();
        return LikeListDTO.builder()
                .id(like.getId())
                .who(username)
                .build();
    }

    public static List<LikeDTO> mapLikesToLikeDTO(List<Like> likes) {
        return likes.stream()
                .map(LikeMapper::buildLikeDTO)
                .collect(Collectors.toList());
    }

    public static List<LikeListDTO> mapLikeListToLikeListDTO(List<Like> likes) {
        return likes.stream()
                .map(LikeMapper::buildLikeListDTO)
                .collect(Collectors.toList());
    }
}