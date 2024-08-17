package blog.article.controllers;

import blog.TestData;
import blog.article.assemblers.ArticleModelAssembler;
import blog.article.controllers.exceptions.ArticleNotFoundException;
import blog.article.domain.Article;
import blog.article.domain.Tag;
import blog.article.services.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

    @MockBean
    private ArticleModelAssembler assembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenArticleObject_whenCreateArticle_thenReturnSavedArticle() throws Exception {

        // given - precondition or setup
        final Article article = TestData.testArticle();

        EntityModel<Article> articleEntityModel = EntityModel.of(article,
                linkTo(methodOn(ArticleController.class).getArticle(article.getId())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));

        // when - action or behaviour that we are going test
        when(service.saveArticle(any(Article.class))).thenReturn(article);
        when(assembler.toModel(any(Article.class))).thenReturn(articleEntityModel);

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
                .andExpect(jsonPath("$.publishDate", is(article.getPublishDate().toString())))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.articles.href").exists());
    }

    // JUnit test for GET All articles REST API
    @Test
    void givenListOfArticles_whenGetAllArticles_thenReturnArticlesList() throws Exception {

        // given - precondition or setup
        final Article article = TestData.testArticle();

        List<EntityModel<Article>> listOfArticles = Arrays.asList(EntityModel.of(article));
        CollectionModel<EntityModel<Article>> collectionModel = CollectionModel.of(listOfArticles);

        // when - action or the behaviour that we are going test
        when(service.getAllArticles()).thenReturn(Arrays.asList(article));
        when(assembler.toModel(any(Article.class))).thenReturn(EntityModel.of(article));

        ResultActions response = mockMvc.perform(get("/articles")
                .accept(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.articleList[0].title").value(article.getTitle()));
    }

    // positive scenario - valid Article id
    // JUnit test for GET Article by id REST API
    @Test
    void givenArticleId_whenGetArticleById_thenReturnArticleObject() throws Exception {

        // given - precondition or setup
        Long articleId = 1L;
        Article article = TestData.testArticle();

        EntityModel<Article> articleEntityModel = EntityModel.of(article);

        // when - action or behaviour that we are going test
        when(service.getArticleById(articleId)).thenReturn(article);
        when(assembler.toModel(any(Article.class))).thenReturn(articleEntityModel);

        ResultActions response = mockMvc.perform(get("/articles/{id}", articleId)
                .accept(MediaType.APPLICATION_JSON));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(article.getId().intValue())))
                .andExpect(jsonPath("$.title", is(article.getTitle())))
                .andExpect(jsonPath("$.content", is(article.getContent())))
                .andExpect(jsonPath("$.tags[0].name", is(article.getTags().get(0).getName())))
                .andExpect(jsonPath("$.publishDate", is(article.getPublishDate().toString())));
    }

    // negative scenario - invalid article id
    // JUnit test for GET article by id REST API
    @Test
    void givenInvalidArticleId_whenGetArticleById_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        Long articleId = 99L;

        when(service.getArticleById(articleId)).thenThrow(new ArticleNotFoundException(articleId));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/articles/{id}", articleId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for update article REST API - positive scenario
    @Test
    void givenUpdatedArticle_whenUpdateArticle_thenReturnUpdateArticleObject() throws Exception {

        // given - condition or setup
        Long articleId = 1L;
        Article savedArticle = TestData.testArticle();

        Tag tag = Tag.builder().id(1L).name("Other Tag name").build();
        Article updatedArticle = Article.builder()
                .id(articleId)
                .title("Updated Title Article")
                .content("Updated content of article")
                .tags(List.of(tag))
                .publishDate(LocalDate.now())
                .build();

        given(service.updateArticle(eq(articleId), any(Article.class))).willReturn(updatedArticle);

        EntityModel<Article> articleEntityModel = EntityModel.of(updatedArticle,
                linkTo(methodOn(ArticleController.class).getArticle(articleId)).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));

        given(assembler.toModel(any(Article.class))).willReturn(articleEntityModel);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/articles/{id}", articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedArticle)));

        // then - verify the output
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(articleId.intValue())))
                .andExpect(jsonPath("$.title", is(updatedArticle.getTitle())))
                .andExpect(jsonPath("$.content", is(updatedArticle.getContent())))
                .andExpect(jsonPath("$.tags[0].name", is(updatedArticle.getTags().get(0).getName())))
                .andExpect(jsonPath("$.publishDate", is(updatedArticle.getPublishDate().toString())));
    }

    // JUnit test for update article REST API - negative scenario
    @Test
    void givenUpdatedArticle_whenUpdateArticle_thenReturn404() throws Exception {

        // given - precondition or setup
        Long articleId = 1L;
        Article updatedArticle = TestData.testArticle();

        // when - action or the behaviour that we are going test
        when(service.updateArticle(eq(articleId), any(Article.class))).thenThrow(new ArticleNotFoundException(articleId));

        ResultActions response = mockMvc.perform(put("/articles/{id}", articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedArticle)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete article REST API
    @Test
    void givenArticleId_whenDeleteArticle_thenReturn200() throws Exception {

        // given - precondition or setup
        Long articleId = 1L;
        willDoNothing().given(service).deleteArticle(articleId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/articles/{id}", articleId));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}
