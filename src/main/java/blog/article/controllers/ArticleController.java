package blog.article.controllers;

import blog.article.assemblers.ArticleModelAssembler;
import blog.article.controllers.exceptions.ArticleNotFoundException;
import blog.article.domain.ArticleCreateRequest;
import blog.article.domain.ArticleResponse;
import blog.article.domain.ArticleUpdateRequest;
import blog.article.services.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerError;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/v1/articles")
public class ArticleController {

    private final ArticleService service;

    private final ArticleModelAssembler assembler;

    public ArticleController(ArticleService service, ArticleModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    @Operation(
            tags = "Article",
            summary = "Register a new article",
            description = "Register a new article to the blog"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article created",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "500", description = "Invalid input",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ServerError.class)))
    })
    public ResponseEntity<?> createArticle(@RequestBody ArticleCreateRequest article) {

        EntityModel<ArticleResponse> savedArticle =  assembler.toModel(service.saveArticle(article));

        return ResponseEntity
                .created(savedArticle.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(savedArticle);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    @Operation(
            tags = "Article",
            summary = "Return all registered articles",
            description = "Return all registered articles of blog"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "failed to get all articles",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ServerError.class))),
    })
    public CollectionModel<EntityModel<ArticleResponse>> getAllArticles() {

        List<EntityModel<ArticleResponse>> articles = service.getAllArticles().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(path = "/{id}")
    @Operation(
            tags = "Article",
            summary = "Return a specific article by ID",
            description = "Return a specific registered article of blog"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ArticleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Article not found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ArticleNotFoundException.class))),
            @ApiResponse(responseCode = "500", description = "failed to get article",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ServerError.class))),
    })
    public EntityModel<ArticleResponse> getArticle(@PathVariable Long id) {

        ArticleResponse foundArticle = service.getArticleById(id);
        return assembler.toModel(foundArticle);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    @Operation(
            tags = "Article",
            summary = "Update article by ID",
            description = "Update a specific registered article of blog"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "failed to update article",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ServerError.class))),
    })
    public ResponseEntity<?> updateArticle(@PathVariable Long id, @RequestBody ArticleUpdateRequest article) {

        ArticleResponse updatedArticle = service.updateArticle(id, article);
        EntityModel<ArticleResponse> articleDTOEntityModel = assembler.toModel(updatedArticle);
        return ResponseEntity.ok(articleDTOEntityModel);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/{id}")
    @Operation(
            tags = "Article",
            summary = "Delete article by ID",
            description = "Delete a specific registered article of blog"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "no content"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    public ResponseEntity<?> deleteArticle(@PathVariable final Long id) {

        service.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/filter")
    @Operation(
            tags = "Article",
            summary = "Return articles by publish date",
            description = "Return articles by publish date"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "failed to get articles by publish date",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ServerError.class))),
    })
    public CollectionModel<EntityModel<ArticleResponse>> getArticlesByPublishDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDate
    ) {

        List<EntityModel<ArticleResponse>> articles = service.getArticlesByDate(publishDate).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/tags/{tagName}")
    @Operation(
            tags = "Article",
            summary = "Return articles by tag name",
            description = "Return articles by tag name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "500", description = "failed to get articles by publish date",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ServerError.class))),
    })
    public CollectionModel<EntityModel<ArticleResponse>> getArticlesByTagName(@PathVariable String tagName) {

        List<EntityModel<ArticleResponse>> articles = service.getArticlesByTagName(tagName).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).getAllArticles()).withSelfRel());
    }
}
