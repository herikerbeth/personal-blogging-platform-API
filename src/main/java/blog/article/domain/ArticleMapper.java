package blog.article.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishDate", expression = "java(java.time.LocalDate.now())")
    ArticleEntity toEntity(ArticleCreateRequest request);

    @Mapping(target = "publishDate", expression = "java(java.time.LocalDate.now())")
    ArticleEntity toEntity(Long id, ArticleUpdateRequest request);

    ArticleResponse toResponse(ArticleEntity entity);
}
