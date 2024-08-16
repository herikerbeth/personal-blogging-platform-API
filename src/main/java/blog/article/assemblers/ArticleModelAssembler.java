package blog.article.assemblers;

import blog.article.controllers.ArticleController;
import blog.article.domain.Article;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class ArticleModelAssembler implements RepresentationModelAssembler<Article, EntityModel<Article>> {

    @Override
    public EntityModel<Article> toModel(Article article) {

        return EntityModel.of(article,
                WebMvcLinkBuilder.linkTo(methodOn(ArticleController.class).getArticle(article.getId())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));
    }
}
