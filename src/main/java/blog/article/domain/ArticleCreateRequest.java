package blog.article.domain;

import java.util.List;

public record ArticleCreateRequest(
        String title,
        String content,
        List<Tag> tags
) {}
