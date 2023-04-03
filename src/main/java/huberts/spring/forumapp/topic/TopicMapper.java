package huberts.spring.forumapp.topic;

import huberts.spring.forumapp.category.Category;
import huberts.spring.forumapp.comment.Comment;
import huberts.spring.forumapp.comment.CommentMapper;
import huberts.spring.forumapp.like.Like;
import huberts.spring.forumapp.like.LikeMapper;
import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import huberts.spring.forumapp.topic.dto.TopicCreateDTO;
import huberts.spring.forumapp.topic.dto.TopicDTO;
import huberts.spring.forumapp.user.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicMapper {

    public static Topic buildNewTopic(TopicCreateDTO topic, Category category, User author) {
        List<Like> emptyLikes = new ArrayList<>();
        List<Comment> emptyComments = new ArrayList<>();
        return Topic.builder()
                .title(topic.getTitle())
                .content(topic.getContent())
                .user(author)
                .category(category)
                .closed(false)
                .createdAt(LocalDateTime.now())
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
                .isClosed(topic.isClosed())
                .categoryName(categoryName)
                .createdTime(topic.getCreatedAt())
                .lastEdit(topic.getLastEdit())
                .likes(LikeMapper.mapLikesToLikeDTO(topic.getLikes()))
                .comments(CommentMapper.mapToCommentDTO(topic.getComments()))
                .build();
    }

    public static List<TopicDTO> mapFromListTopic(List<Topic> topics) {
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

    public static List<ShortTopicDTO> mapShortTopicDTOList(List<Topic> topics) {
        return topics.stream()
                .map(TopicMapper::buildShortTopicDTO)
                .collect(Collectors.toList());
    }
}