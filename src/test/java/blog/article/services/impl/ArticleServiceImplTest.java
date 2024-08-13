package blog.article.services.impl;

import blog.article.domain.Article;
import blog.article.domain.ArticleEntity;
import blog.article.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static blog.TestData.testArticle;
import static blog.TestData.testArticleEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl underTest;

    @Test
    void test_That_Article_Is_Saved() {

        // given - precondition or setup
        final Article article = testArticle();

        final ArticleEntity articleEntity = testArticleEntity();

        // when - action or behaviour that we are going test
        when(articleRepository.save(eq(articleEntity))).thenReturn(articleEntity);

        final Article result = underTest.saveArticle(article);

        // then - verify the result or output using assert statements
        assertEquals(article, result);
    }
}
