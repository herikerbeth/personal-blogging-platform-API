package blog.article.controller;

import blog.article.model.Article;
import blog.article.service.ArticleService;
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

        var articleCreated = service.createArticle(article);
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

        var article = service.findById(id);
        return ResponseEntity.ok(article);
    }
}
