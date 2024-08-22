package blog.article.controllers;

import blog.article.assemblers.ArticleModelAssembler;
import blog.article.domain.ArticleCreateRequest;
import blog.article.domain.ArticleResponse;
import blog.article.domain.ArticleUpdateRequest;
import blog.article.services.ArticleService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
public class ArticleController {

    private final ArticleService service;

    private final ArticleModelAssembler assembler;

    public ArticleController(ArticleService service, ArticleModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(path = "/articles")
    public ResponseEntity<?> createArticle(@RequestBody ArticleCreateRequest article) {

        EntityModel<ArticleResponse> savedArticle =  assembler.toModel(service.saveArticle(article));

        return ResponseEntity
                .created(savedArticle.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(savedArticle);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(path = "/articles")
    public CollectionModel<EntityModel<ArticleResponse>> getAllArticles() {

        List<EntityModel<ArticleResponse>> articles = service.getAllArticles().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(path = "/articles/{id}")
    public EntityModel<ArticleResponse> getArticle(@PathVariable Long id) {

        ArticleResponse foundArticle = service.getArticleById(id);
        return assembler.toModel(foundArticle);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/articles/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Long id, @RequestBody ArticleUpdateRequest article) {

        ArticleResponse updatedArticle = service.updateArticle(id, article);
        EntityModel<ArticleResponse> articleDTOEntityModel = assembler.toModel(updatedArticle);
        return ResponseEntity.ok(articleDTOEntityModel);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable final Long id) {

        service.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
