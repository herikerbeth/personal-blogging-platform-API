package blog.article.domain;

import java.util.List;

public record ArticleUpdateRequest(
        String title,
        String content,
        List<Tag> tags
) {}
