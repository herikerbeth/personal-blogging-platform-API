package blog.article.service;

import blog.article.model.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ArticleService {

    List<Article> getAllArticles();
    Article findById(Long id);
    Article createArticle(Article article);
    void deleteArticle(Long id);
    Article updateArticle(Article article, Long id);

}
