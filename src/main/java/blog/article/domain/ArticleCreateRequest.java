package blog.article.domain;

import blog.tag.domain.TagEntity;
import blog.tag.domain.TagCreateRequest;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ArticleCreateRequest(
        @Schema(description = "Title of the article", example = "John")
        String title,
        @Schema(description = "Content of the article", example = "content")
        String content,
        @ArraySchema(schema = @Schema(description = "Tags of the article", implementation = TagCreateRequest.class))
        List<TagEntity> tags
) {}
