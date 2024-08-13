package blog;

import blog.article.domain.Article;
import blog.article.domain.Tag;

import java.time.LocalDate;
import java.util.List;

public class TestData {

    public TestData() {
    }

    public static Article testArticle() {

        Tag tag = Tag.builder().id(1L).name("Tag name").build();
        return Article.builder()
                .id(1L)
                .title("Title Article")
                .content("Content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();
    }
}
