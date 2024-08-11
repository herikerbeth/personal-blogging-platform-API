package blog.article.service;

import blog.article.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleService {

    Article saveArticle(Article article);
    List<Article> getAllArticles();
    Optional<Article> getArticleById(Long id);
    Article updateArticle(Article article);
    void deleteArticle(Long id);

}
