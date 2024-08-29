package blog.article.domain;

import blog.tag.domain.TagEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "articles")
@Table(name = "articles")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TagEntity> tags;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    public ArticleEntity(ArticleCreateRequest data) {
        this.title = data.title();
        this.content = data.content();
        this.tags = data.tags();
        this.publishDate = LocalDate.now();
    }

    public ArticleEntity(ArticleUpdateRequest data) {
        this.title = data.title();
        this.content = data.content();
        this.tags = data.tags();
        this.publishDate = LocalDate.now();
    }
}
