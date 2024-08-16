package blog.article.controllers;

import blog.article.assemblers.ArticleModelAssembler;
import blog.article.domain.Article;
import blog.article.services.ArticleService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<?> createArticle(@RequestBody Article article) {

        EntityModel<Article> savedArticle =  assembler.toModel(service.saveArticle(article));

        return ResponseEntity
                .created(savedArticle.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(savedArticle);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(path = "/articles")
    public CollectionModel<EntityModel<Article>> getAllArticles() {

        List<EntityModel<Article>> articles = service.getAllArticles().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable final Long id) {

        final Optional<Article> foundArticle = service.getArticleById(id);
        return foundArticle
                .map(article -> new ResponseEntity<>(article, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {

        return service.getArticleById(id)
                .map(savedArticle -> {

                    savedArticle.setTitle(article.getTitle());
                    savedArticle.setContent(article.getContent());
                    savedArticle.setTags(article.getTags());
                    savedArticle.setPublishDate(article.getPublishDate());

                    Article updatedArticle = service.updateArticle(savedArticle);
                    return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable final Long id) {

        service.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
