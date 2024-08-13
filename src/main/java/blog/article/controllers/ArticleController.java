package blog.article.controllers;

import blog.article.domain.Article;
import blog.article.services.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody final Article article) {

        final Article savedArticle = service.saveArticle(article);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedArticle.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedArticle);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {

        return new ResponseEntity<>(service.getAllArticles(), HttpStatus.OK);
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
