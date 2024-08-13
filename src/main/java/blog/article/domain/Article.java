package blog.article.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
 
    private Long id;
    private String title;
    private String content;
    private List<Tag> tags;
    private LocalDate publishDate;
}
