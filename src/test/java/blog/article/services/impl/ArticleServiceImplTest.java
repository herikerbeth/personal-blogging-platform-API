package blog.article.services.impl;

import blog.article.controllers.exceptions.ArticleNotFoundException;
import blog.article.domain.*;
import blog.article.repositories.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static blog.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl underTest;

    @Test
    void test_That_Article_Is_Saved() {

        // given - precondition or setup
        final ArticleCreateRequest article = testArticleRequestDTO();

        final ArticleEntity articleEntity = new ArticleEntity(article);

        final ArticleResponse expectedResponse = new ArticleResponse(articleEntity);

        // when - action or behaviour that we are going test
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(articleEntity);

        final ArticleResponse result = underTest.saveArticle(article);

        // then - verify the result or output using assert statements
        assertEquals(expectedResponse, result);
    }

    @Test
    void test_List_Articles_Returns_Articles_When_Exist() {

        // given - precondition or setup
        final ArticleEntity articleEntity = testArticleEntity();

        // when - action or behaviour that we are going test
        when(articleRepository.findAll()).thenReturn(List.of(articleEntity));

        final List<ArticleResponse> result = underTest.getAllArticles();

        // then - verify the result or output using assert statements
        assertEquals(1, result.size());
    }

    @Test
    void test_List_Articles_Returns_Empty_List_When_No_Articles_Exist() {

        // when - action or behaviour that we are going test
        when(articleRepository.findAll()).thenReturn(new ArrayList<>());

        final List<ArticleResponse> result = underTest.getAllArticles();

        // then - verify the result or output using assert statements
        assertEquals(0, result.size());
    }

    @Test
    void test_That_Find_By_Id_Returns_Article_When_Exists() {

        // given - precondition or setup
        final Long articleId = 1L;
        final ArticleEntity articleEntity = testArticleEntity();
        final ArticleResponse article = new ArticleResponse(articleEntity);

        // when - action or behaviour that we are going test
        when(articleRepository.findById(eq(articleId))).thenReturn(Optional.of(articleEntity));

        final ArticleResponse result = underTest.getArticleById(articleId);

        // then - verify the result or output using assert statements
        assertEquals(article, result);
    }

    @Test
    void test_That_Find_By_Id_Throws_Exception_When_No_Article() {

        // given - precondition or setup
        final Long id = 1L;

        // when - action or behaviour that we are going test
        when(articleRepository.findById(eq(id))).thenReturn(Optional.empty());

        // then - verify the result or output using assert statements
        assertThrows(ArticleNotFoundException.class, () -> underTest.getArticleById(id));
    }

    @Test
    void test_That_Article_Is_Updated() {

        // given - precondition or setup
        final Long id = 1L;
        final ArticleUpdateRequest updatedArticle = testArticleUpdateDTO();
        final ArticleEntity articleEntity = new ArticleEntity(updatedArticle);
        final ArticleResponse expectedResponse = new ArticleResponse(articleEntity);

        // when - action or behaviour that we are going test
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(articleEntity);

        final ArticleResponse result = underTest.updateArticle(id, updatedArticle);

        // then - verify the result or output using assert statements
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void test_That_UpdateArticle_Throws_Exception_When_No_ArticleId() {

        // given - precondition or setup
        final Long articleId = 1L;
        final ArticleUpdateRequest updatedArticle = testArticleUpdateDTO();

        // when - action or behaviour that we are going test
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(null);

        // then - verify the result or output using assert statements
        assertThrows(NullPointerException.class, () -> {
            underTest.updateArticle(articleId, updatedArticle);
        });
    }

    @Test
    void test_Delete_Article_Deletes_Article() {

        // given - precondition or setup
        final Long id = 1L;

        // when - action or behaviour that we are going test
        underTest.deleteArticle(id);

        // then - verify the result or output using assert statements
        verify(articleRepository, times(1)).deleteById(eq(id));
    }

    @Test
    void test_Delete_Article_ThrowsEmptyResultDataAccessException() {

        final Long articleId = 1L;

        doThrow(new EmptyResultDataAccessException(1)).when(articleRepository).deleteById(articleId);

        underTest.deleteArticle(articleId);

        verify(articleRepository, times(1)).deleteById(articleId);
    }
}
