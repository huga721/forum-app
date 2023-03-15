package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.like.LikeMapper;
import huberts.spring.forumapp.like.dto.LikeListDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public static Comment buildNewComment(User user, Topic topic, String content) {
        return Comment.builder()
                .createdAt(LocalDateTime.now())
                .content(content)
                .user(user)
                .topic(topic)
                .likes(List.of())
                .build();
    }

    public static CommentDTO buildCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .author(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .content(comment.getContent())
                .likes(mapToLikeDTO(comment.getLikes()))
                .build();
    }

    public static List<CommentDTO> mapToCommentDTO(List<Comment> comments){
        return comments.stream()
                .map(CommentMapper::buildCommentDTO)
                .collect(Collectors.toList());
    }

    private static List<LikeListDTO> mapToLikeDTO(List<Like> likes) {
        return LikeMapper.mapLikesToLikeDTO(likes);
    }
}