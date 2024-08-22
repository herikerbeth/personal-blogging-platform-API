package blog.article.services.impl;

import blog.article.controllers.exceptions.ArticleNotFoundException;
import blog.article.domain.*;
import blog.article.repositories.ArticleRepository;
import blog.article.services.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper = ArticleMapper.INSTANCE;

    @Autowired
    public ArticleServiceImpl(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public ArticleResponse saveArticle(ArticleCreateRequest article) {

        ArticleEntity articleEntity = articleMapper.toEntity(article);
        ArticleEntity savedArticle = articleRepository.save(articleEntity);
        return articleMapper.toResponse(savedArticle);
    }

    @Override
    public List<ArticleResponse> getAllArticles() {

        return articleRepository.findAll().stream()
                .map(articleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ArticleResponse getArticleById(Long id) {

        ArticleEntity foundArticle = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));
        return articleMapper.toResponse(foundArticle);
    }

    @Override
    public ArticleResponse updateArticle(Long id, ArticleUpdateRequest updateArticleDTO) {

        ArticleEntity articleToUpdate = articleMapper.toEntity(id, updateArticleDTO);
        ArticleEntity updatedArticle = articleRepository.save(articleToUpdate);
        return articleMapper.toResponse(updatedArticle);
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
