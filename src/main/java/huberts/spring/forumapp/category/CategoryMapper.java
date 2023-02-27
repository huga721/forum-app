package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CategoryCreateDTO;
import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.category.dto.ShortCategoryDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicMapper;
import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public static Category buildNewCategory(CategoryCreateDTO create) {
        List<Topic> emptyList = new ArrayList<>();
        return Category.builder()
                .title(create.getTitle())
                .description(create.getDescription())
                .topics(emptyList)
                .build();
    }

    public static CategoryDTO buildCategoryDTO(Category category) {
        List<Topic> topics = category.getTopics();
        return CategoryDTO.builder()
                .title(category.getTitle())
                .description(category.getDescription())
                .topics(returnShortTopicDTO(topics))
                .build();
    }

    public static List<CategoryDTO> mapFromListCategory(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::buildCategoryDTO)
                .collect(Collectors.toList());
    }

    public static ShortCategoryDTO buildShortCategoryDTO(Category category) {
        return ShortCategoryDTO.builder()
                .title(category.getTitle())
                .description(category.getDescription())
                .build();
    }

    public static List<ShortTopicDTO> returnShortTopicDTO(List<Topic> topics) {
        return TopicMapper.mapShortTopicDTOList(topics);
    }
}