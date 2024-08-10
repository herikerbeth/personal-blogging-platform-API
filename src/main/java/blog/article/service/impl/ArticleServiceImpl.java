package blog.article.service.impl;

import blog.article.model.Article;
import blog.article.repository.ArticleRepository;
import blog.article.service.ArticleService;

import java.util.List;
import java.util.NoSuchElementException;

public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Article createArticle(Article article) {

        if (articleRepository.existsById(article.getId())) {
            throw new IllegalArgumentException("Article already exists!");
        }
        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public Article updateArticle(Article newArticle, Long id) {

        return articleRepository.findById(id)
                .map(article -> {
                    article.setTitle(article.getTitle());
                    article.setContent(article.getContent());
                    article.setTags(article.getTags());
                    article.setPublishDate(article.getPublishDate());
                    return articleRepository.save(article);
                })
                .orElseGet(() -> {
                    return articleRepository.save(newArticle);
                });
    }
}
