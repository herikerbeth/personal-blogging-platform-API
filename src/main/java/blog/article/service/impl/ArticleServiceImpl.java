package blog.article.service.impl;

import blog.article.model.Article;
import blog.article.repository.ArticleRepository;
import blog.article.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article saveArticle(Article article) {

        if (articleRepository.existsById(article.getId())) {
            throw new IllegalArgumentException("Article already exists!");
        }
        return articleRepository.save(article);
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> getArticleById(Long id) {

        return articleRepository.findById(id);
    }

    @Override
    public Article updateArticle(Article updatedArticle) {

        return articleRepository.save(updatedArticle);
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
