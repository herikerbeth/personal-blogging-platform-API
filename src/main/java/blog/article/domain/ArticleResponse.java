package blog.article.domain;

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
        @Schema(description = "Tags of the article")
        List<Tag> tags,
        @Schema(description = "Publish date of the article", example = "2024-08-24")
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
