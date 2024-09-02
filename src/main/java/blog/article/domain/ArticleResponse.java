package blog.article.domain;

import blog.tag.domain.TagEntity;
import blog.tag.domain.TagResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ArticleResponse(
        @Schema(description = "ID of the article", example = "1")
        Long id,
        @Schema(description = "Title of the article", example = "John")
        String title,
        @Schema(description = "Content of the article", example = "content")
        String content,
        @ArraySchema(schema = @Schema(description = "Tags of the article", implementation = TagResponse.class))
        List<TagEntity> tags,
        @Schema(description = "Publish date of the article", example = "java.time.LocalDate.now()")
        LocalDate publishDate
) {

    public ArticleResponse(ArticleEntity articleEntity) {
        this(
                articleEntity.getId(),
                articleEntity.getTitle(),
                articleEntity.getContent(),
                articleEntity.getTags(),
                articleEntity.getPublishDate()
        );
    }
}
