package blog.article.assemblers;

import blog.article.controllers.ArticleController;
import blog.article.domain.ArticleResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class ArticleModelAssembler implements RepresentationModelAssembler<ArticleResponse, EntityModel<ArticleResponse>> {

    @Override
    public EntityModel<ArticleResponse> toModel(ArticleResponse article) {

        return EntityModel.of(article,
                WebMvcLinkBuilder.linkTo(methodOn(ArticleController.class).getArticle(article.id())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));
    }
}
