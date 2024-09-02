package blog.article.services.impl;

import blog.article.controllers.exceptions.ArticleNotFoundException;
import blog.article.domain.*;
import blog.article.repositories.ArticleRepository;
import blog.article.services.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
    public ArticleResponse saveArticle(ArticleCreateRequest article) {

        ArticleEntity articleEntity = new ArticleEntity(article);
        ArticleEntity articleEntitySaved = articleRepository.save(articleEntity);
        return new ArticleResponse(articleEntitySaved);
    }

    @Override
    public List<ArticleResponse> getAllArticles() {

        return articleRepository.findAll().stream()
                .map(ArticleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleResponse getArticleById(Long id) {

        ArticleEntity foundArticle = articleRepository.findById(id).
                orElseThrow(() -> new ArticleNotFoundException(id));
        return new ArticleResponse(foundArticle);
    }

    @Override
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest updateArticleDTO) {

        ArticleEntity articleToUpdate = new ArticleEntity(updateArticleDTO);
        articleToUpdate.setId(id);
        ArticleEntity updatedArticle = articleRepository.save(articleToUpdate);
        return new ArticleResponse(updatedArticle);
    }

    @Override
    public void deleteArticle(Long id) {

        try {
            articleRepository.deleteById(id);
        } catch (final EmptyResultDataAccessException ex) {
            log.debug("Attempted to delete non-existing article", ex);
        }
    }

    @Override
    public List<ArticleResponse> getArticlesByDate(LocalDate date) {

        return articleRepository.findAllByPublishDate(date).stream()
                .map(ArticleResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleResponse> getArticlesByTagName(String tagName) {

        return articleRepository.findAllByTagsName(tagName).stream()
                .map(ArticleResponse::new)
                .collect(Collectors.toList());
    }
}
