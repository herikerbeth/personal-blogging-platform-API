package blog.tag.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagResponse(
        @Schema(description = "ID of the tag", example = "1")
        Long id,
        @Schema(description = "Name of the tag", example = "tag name")
        String name
) {

    public TagResponse(TagEntity tagEntity) {
        this(
                tagEntity.getId(),
                tagEntity.getName()
        );
    }
}
