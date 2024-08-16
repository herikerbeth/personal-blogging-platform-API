package blog.article.services.impl;

import blog.article.domain.Article;
import blog.article.domain.ArticleEntity;
import blog.article.repositories.ArticleRepository;
import blog.article.services.ArticleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article saveArticle(Article article) {
        ArticleEntity articleEntity = ArticleEntity.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .publishDate(article.getPublishDate())
                .tags(article.getTags())
                .build();
        ArticleEntity savedArticle = articleRepository.save(articleEntity);

        return new Article(savedArticle.getId(), savedArticle.getTitle(), savedArticle.getContent(),
                savedArticle.getTags(), savedArticle.getPublishDate());
    }

    @Override
    public List<Article> getAllArticles() {

        return articleRepository.findAll().stream()
                .map(article -> new Article(article.getId(), article.getTitle(), article.getContent(),
                        article.getTags(), article.getPublishDate()))
                .collect(Collectors.toList());
    }

    @Override
    public Article getArticleById(Long id) {

        ArticleEntity foundArticle = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return new Article(foundArticle.getId(), foundArticle.getTitle(), foundArticle.getContent(),
                foundArticle.getTags(), foundArticle.getPublishDate());
    }

    @Override
    public Article updateArticle(Article updatedArticle) {

        final ArticleEntity articleEntity = articleToArticleEntity(updatedArticle);
        final ArticleEntity savedArticleEntity = articleRepository.save(articleEntity);
        return articleEntityToArticle(savedArticleEntity);
    }

    @Override
    public void deleteArticle(Long id) {

        try {
            articleRepository.deleteById(id);
        } catch (final EmptyResultDataAccessException ex) {
            log.debug("Attempted to delete non-existing article", ex);
        }
    }
}
