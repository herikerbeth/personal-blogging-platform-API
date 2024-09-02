package blog.tag.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagCreateRequest(
        @Schema(description = "Name of the tag", example = "tag name")
        String name
) {}
