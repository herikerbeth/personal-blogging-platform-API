package blog.article.domain;

import java.time.LocalDate;
import java.util.List;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        List<Tag> tags,
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
