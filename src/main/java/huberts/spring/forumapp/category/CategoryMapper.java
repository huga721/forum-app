package huberts.spring.forumapp.category;

import huberts.spring.forumapp.category.dto.CreateCategoryDTO;
import huberts.spring.forumapp.category.dto.CategoryDTO;
import huberts.spring.forumapp.topic.Topic;
import huberts.spring.forumapp.topic.TopicMapper;
import huberts.spring.forumapp.topic.dto.ShortTopicDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public static Category buildCategory(CreateCategoryDTO createCategoryDTO) {
        List<Topic> emptyList = new ArrayList<>();
        return Category.builder()
                .title(createCategoryDTO.title())
                .description(createCategoryDTO.description())
                .topics(emptyList)
                .build();
    }

    public static CategoryDTO buildCategoryDTO(Category category) {
        List<Topic> topics = category.getTopics();
        return CategoryDTO.builder()
                .title(category.getTitle())
                .description(category.getDescription())
                .topics(mapTopicListToShortTopicDTOList(topics))
                .build();
    }

    public static List<CategoryDTO> mapCategoryListToCategoryDTOList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::buildCategoryDTO)
                .collect(Collectors.toList());
    }

    private static List<ShortTopicDTO> mapTopicListToShortTopicDTOList(List<Topic> topics) {
        return TopicMapper.mapTopicListToShortTopicDTOList(topics);
    }
}