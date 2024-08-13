package blog.article.services.impl;

import blog.article.domain.Article;
import blog.article.domain.ArticleEntity;
import blog.article.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static blog.TestData.testArticle;
import static blog.TestData.testArticleEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void test_List_Articles_Returns_Articles_When_Exist() {

        // given - precondition or setup
        final ArticleEntity articleEntity = testArticleEntity();

        // when - action or behaviour that we are going test
        when(articleRepository.findAll()).thenReturn(List.of(articleEntity));

        final List<Article> result = underTest.getAllArticles();

        // then - verify the result or output using assert statements
        assertEquals(1, result.size());
    }

    @Test
    void test_List_Articles_Returns_Empty_List_When_No_Articles_Exist() {

        // when - action or behaviour that we are going test
        when(articleRepository.findAll()).thenReturn(new ArrayList<>());

        final List<Article> result = underTest.getAllArticles();

        // then - verify the result or output using assert statements
        assertEquals(0, result.size());
    }

    @Test
    void test_That_Find_By_Id_Returns_Article_When_Exists() {

        // given - precondition or setup
        final Article article = testArticle();

        final ArticleEntity articleEntity = testArticleEntity();

        // when - action or behaviour that we are going test
        when(articleRepository.findById(eq(article.getId()))).thenReturn(Optional.of(articleEntity));

        final Optional<Article> result = underTest.getArticleById(article.getId());

        // then - verify the result or output using assert statements
        assertEquals(Optional.of(article), result);
    }

    @Test
    void test_That_Find_By_Id_Returns_Empty_When_No_Article() {

        // given - precondition or setup
        final Long id = 1L;

        // when - action or behaviour that we are going test
        when(articleRepository.findById(eq(id))).thenReturn(Optional.empty());

        final Optional<Article> result = underTest.getArticleById(id);

        // then - verify the result or output using assert statements
        assertEquals(Optional.empty(), result);
    }

    @Test
    void test_That_Article_Is_Updated() {

        // given - precondition or setup
        final Article updatedArticle = testArticle();
        final ArticleEntity updatedArticleEntity = articleToArticleEntity(updatedArticle);

        // when - action or behaviour that we are going test
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(updatedArticleEntity);

        final Article result = underTest.updateArticle(updatedArticle);

        // then - verify the result or output using assert statements
        assertEquals(updatedArticle, result);
    }

    private ArticleEntity articleToArticleEntity(Article article) {

        return ArticleEntity.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .tags(article.getTags())
                .publishDate(article.getPublishDate())
                .build();
    }
}
