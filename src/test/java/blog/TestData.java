package blog;

import blog.article.domain.*;

import java.time.LocalDate;
import java.util.List;

public class TestData {

    private static final Tag tag = Tag.builder().id(1L).name("Tag name").build();

    public TestData() {
    }

    public static ArticleCreateRequest testArticleRequestDTO() {

        return new ArticleCreateRequest("Title Article", "Content of article", List.of(tag));
    }

    public static ArticleResponse testArticleResponseDTO() {

        return new ArticleResponse(1L, "Title Article", "Content of article", List.of(tag),
                LocalDate.now());
    }

    public static ArticleUpdateRequest testArticleUpdateDTO() {

        return new ArticleUpdateRequest("Title Article", "Content of article", List.of(tag));
    }

    public static ArticleEntity testArticleEntity() {

        return ArticleEntity.builder()
                .id(1L)
                .title("Title Article")
                .content("Content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();
    }
}
