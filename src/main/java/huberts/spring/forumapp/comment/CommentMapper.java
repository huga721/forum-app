package huberts.spring.forumapp.comment;

import huberts.spring.forumapp.comment.dto.CommentDTO;
import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.like.LikeMapper;
import huberts.spring.forumapp.like.dto.LikeListDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public static Comment buildComment(User user, Topic topic, String content) {
        return Comment.builder()
                .content(content)
                .user(user)
                .topic(topic)
                .reports(List.of())
                .likes(List.of())
                .build();
    }

    public static CommentDTO buildCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .author(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .reportsReceived(comment.getReports().size())
                .lastEdit(comment.getLastEdit())
                .content(comment.getContent())
                .likes(mapLikeListToLikeListDTO(comment.getLikes()))
                .build();
    }

    public static List<CommentDTO> mapCommentListToCommentDTOList(List<Comment> comments){
        return comments.stream()
                .map(CommentMapper::buildCommentDTO)
                .collect(Collectors.toList());
    }

    private static List<LikeListDTO> mapLikeListToLikeListDTO(List<Like> likes) {
        return LikeMapper.mapLikeListToLikeListDTO(likes);
    }
}