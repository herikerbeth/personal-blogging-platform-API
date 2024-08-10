package blog.article.service;

import blog.article.model.Article;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ArticleService {

    Article saveArticle(Article article);
    List<Article> getAllArticles();
    Optional<Article> getArticleById(Long id);
    Article updateArticle(Article article);
    void deleteArticle(Long id);

}
