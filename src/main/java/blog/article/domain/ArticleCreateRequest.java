package blog.article.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ArticleCreateRequest(
        @Schema(description = "Title of the article", example = "John")
        String title,
        @Schema(description = "Content of the article", example = "content")
        String content,
        List<Tag> tags
) {}
