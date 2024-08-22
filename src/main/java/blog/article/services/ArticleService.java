package blog.article.services;

import blog.article.domain.ArticleCreateRequest;
import blog.article.domain.ArticleResponse;
import blog.article.domain.ArticleUpdateRequest;

import java.util.List;

public interface ArticleService {

    ArticleResponse saveArticle(ArticleCreateRequest article);
    List<ArticleResponse> getAllArticles();
    ArticleResponse getArticleById(Long id);
    ArticleResponse updateArticle(Long id, ArticleUpdateRequest article);
    void deleteArticle(Long id);
}
