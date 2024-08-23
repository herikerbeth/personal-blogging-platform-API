package blog.article.domain;

import blog.TestData;
import blog.article.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleEntityTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void testCreateArticle() {

        // given
        ArticleEntity article = TestData.testArticleEntity();

        // when
        ArticleEntity savedArticle = articleRepository.save(article);

        // then
        assertThat(savedArticle.getId()).isNotNull();
        assertThat(savedArticle.getTitle()).isEqualTo(article.getTitle());
        assertThat(savedArticle.getContent()).isEqualTo(article.getContent());
        assertThat(savedArticle.getTags()).hasSize(1);
        assertThat(savedArticle.getPublishDate()).isNotNull();
    }
}
