package blog.article.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity(name = "articles")
@Table(name = "articles")
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @OneToMany
    private List<Tag> tags;

    @Column(name = "publish_date")
    private LocalDate publishDate;
}
