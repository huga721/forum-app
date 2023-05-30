package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.comment.CommentMapper;
import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.like.LikeMapper;
import huberts.spring.forumapp.topic.dto.CreateTopicDTO;
import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import huberts.spring.forumapp.topic.dto.TopicDTO;
import huberts.spring.forumapp.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicMapper {

    public static Topic buildTopic(CreateTopicDTO topic, Category category, User author) {
        List<Like> emptyLikes = new ArrayList<>();
        List<Comment> emptyComments = new ArrayList<>();
        return Topic.builder()
                .title(topic.title())
                .content(topic.content())
                .user(author)
                .category(category)
                .closed(false)
                .likes(emptyLikes)
                .comments(emptyComments)
                .build();
    }

    public static TopicDTO buildTopicDTO(Topic topic) {
        String categoryName = topic.getCategory().getTitle();
        String username = topic.getUser().getUsername();
        return TopicDTO.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .content(topic.getContent())
                .author(username)
                .closed(topic.isClosed())
                .categoryName(categoryName)
                .createdTime(topic.getCreatedAt())
                .lastEdit(topic.getLastEdit())
                .likes(LikeMapper.mapLikeListToLikeListDTO(topic.getLikes()))
                .comments(CommentMapper.mapCommentListToCommentDTOList(topic.getComments()))
                .build();
    }

    public static List<TopicDTO> mapTopicListToTopicDTOList(List<Topic> topics) {
        return topics.stream()
                .map(TopicMapper::buildTopicDTO)
                .collect(Collectors.toList());
    }

    public static ShortTopicDTO buildShortTopicDTO(Topic topic) {
        String username = topic.getUser().getUsername();
        return ShortTopicDTO.builder()
                .id(topic.getId())
                .author(username)
                .topicTitle(topic.getTitle())
                .topicContent(topic.getContent())
                .likes(topic.getLikes().size())
                .comments(topic.getComments().size())
                .build();
    }

    public static List<ShortTopicDTO> mapTopicListToShortTopicDTOList(List<Topic> topics) {
        return topics.stream()
                .map(TopicMapper::buildShortTopicDTO)
                .collect(Collectors.toList());
    }
}