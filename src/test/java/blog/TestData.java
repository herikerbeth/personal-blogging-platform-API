package blog;

import blog.article.domain.*;
import blog.tag.domain.TagEntity;

import java.time.LocalDate;
import java.util.List;

public class TestData {

    public TestData() {
    }

    public static TagEntity testTagEntity() {

        return new TagEntity(1L, "tag name");
    }

    public static ArticleCreateRequest testArticleRequestDTO() {

        return new ArticleCreateRequest("Title Article", "Content of article", List.of(testTagEntity()));
    }

    public static ArticleResponse testArticleResponseDTO() {

        return new ArticleResponse(1L, "Title Article", "Content of article", List.of(testTagEntity()),
                LocalDate.now());
    }

    public static ArticleUpdateRequest testArticleUpdateDTO() {

        return new ArticleUpdateRequest("Title Article", "Content of article", List.of(testTagEntity()));
    }

    public static ArticleEntity testArticleEntity() {

        return ArticleEntity.builder()
                .id(1L)
                .title("Title Article")
                .content("Content of article")
                .tags(List.of(testTagEntity()))
                .publishDate(LocalDate.now())
                .build();
    }
}
