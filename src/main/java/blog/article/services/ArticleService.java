package blog.article.services;

import blog.article.domain.Article;

import java.util.List;

public interface ArticleService {

    Article saveArticle(Article articleDTO);
    List<Article> getAllArticles();
    Article getArticleById(Long id);
    Article updateArticle(Long id, Article article);
    void deleteArticle(Long id);
}
