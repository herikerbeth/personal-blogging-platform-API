package blog.article.controller;

import blog.article.model.Article;
import blog.article.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {

        var articleCreated = service.saveArticle(article);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(articleCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(articleCreated);
    }

    @GetMapping
    public List<Article> getAllArticles() {

        return service.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {

        return service.getArticleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {

        service.deleteArticle(id);
        return new ResponseEntity<String>("Article deleted successfully!.", HttpStatus.OK);
    }
}
