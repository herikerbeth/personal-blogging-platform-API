package blog.article.domain;

import blog.article.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleEntityTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void testCreateArticle() {

        // given
        Tag tag = Tag.builder()
                .name("Tech")
                .build();

        ArticleEntity article = ArticleEntity.builder()
                .title("New Article")
                .content("This is a new article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();

        // when
        ArticleEntity savedArticle = articleRepository.save(article);

        // then
        assertThat(savedArticle.getId()).isNotNull();
        assertThat(savedArticle.getTitle()).isEqualTo("New Article");
        assertThat(savedArticle.getContent()).isEqualTo("This is a new article");
        assertThat(savedArticle.getTags()).hasSize(1);
        assertThat(savedArticle.getPublishDate()).isNotNull();
    }
}
