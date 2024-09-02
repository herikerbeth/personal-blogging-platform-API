package blog.article.repositories;

import blog.article.domain.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    List<ArticleEntity> findAllByPublishDate(LocalDate publishDate);

    @Query("SELECT a FROM articles a JOIN a.tags t WHERE t.name = :tagName")
    List<ArticleEntity> findAllByTagsName(@Param("tagName")String tagName);
}
