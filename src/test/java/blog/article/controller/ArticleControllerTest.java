package blog.article.controller;

import blog.article.model.Article;
import blog.article.model.Tag;
import blog.article.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService service;

    @Test
    void givenArticleObject_whenCreateArticle_thenReturnSavedArticle() throws Exception {

        // given - precondition or setup
        Tag tag = Tag.builder().name("Tag name").build();
        Article article = Article.builder()
                .id(1L)
                .title("Title Article")
                .content("Content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();

        given(service.saveArticle(any(Article.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(article)));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(article.getId().intValue())))
                .andExpect(jsonPath("$.title", is(article.getTitle())))
                .andExpect(jsonPath("$.content", is(article.getContent())))
                .andExpect(jsonPath("$.tags[0].name", is(article.getTags().get(0).getName())))
                .andExpect(jsonPath("$.publishDate", is(article.getPublishDate().toString())));
    }

    // JUnit test for GET All articles REST API
    @Test
    void givenListOfArticles_whenGetAllArticles_thenReturnArticlesList() throws Exception {

        // given - precondition or setup
        List<Article> listOfArticles = new ArrayList<>();
        Tag tag = Tag.builder().name("Tag name").build();
        Article article = Article.builder()
                .id(1L)
                .title("Title Article")
                .content("Content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();
        Article article1 = Article.builder()
                .id(2L)
                .title("Title Article1")
                .content("Content of article1")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();

        listOfArticles.add(article);
        listOfArticles.add(article1);
        given(service.getAllArticles()).willReturn(listOfArticles);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/articles"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfArticles.size())));
    }

    // positive scenario - valid Article id
    // JUnit test for GET Article by id REST API
    @Test
    void givenArticleId_whenGetArticleById_thenReturnArticleObject() throws Exception {

        // given - precondition or setup
        Long articleId = 1L;
        Tag tag = Tag.builder().name("Tag name").build();
        Article article = Article.builder()
                .id(1L)
                .title("Title Article")
                .content("Content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();

        given(service.getArticleById(articleId))
                .willReturn(Optional.of(article));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/articles/{id}", articleId));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(article.getId().intValue())))
                .andExpect(jsonPath("$.title", is(article.getTitle())))
                .andExpect(jsonPath("$.content", is(article.getContent())))
                .andExpect(jsonPath("$.tags[0].name", is(article.getTags().get(0).getName())))
                .andExpect(jsonPath("$.publishDate", is(article.getPublishDate().toString())));
    }

    // JUnit test for update employee REST API - positive scenario
    @Test
    void givenUpdatedArticle_whenUpdateArticle_thenReturnUpdateArticleObject() throws Exception {

        // given - condition or setup
        Long articleId = 1L;
        Tag tag = Tag.builder().name("Tag name").build();
        Article savedArticle = Article.builder()
                .title("Title Article")
                .content("Content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();

        tag = Tag.builder().name("Other Tag name").build();
        Article updatedArticle = Article.builder()
                .title("Updated Title Article")
                .content("Updated content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();
        given(service.getArticleById(articleId)).willReturn(Optional.of(savedArticle));
        given(service.updateArticle(any(Article.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/articles/{id}", articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedArticle)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(updatedArticle.getTitle())))
                .andExpect(jsonPath("$.content", is(updatedArticle.getContent())))
                .andExpect(jsonPath("$.tags[0].name", is(updatedArticle.getTags().get(0).getName())))
                .andExpect(jsonPath("$.publishDate", is(updatedArticle.getPublishDate().toString())));
    }
}
