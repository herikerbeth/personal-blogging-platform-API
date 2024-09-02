package blog.article.controllers;

import blog.TestData;
import blog.article.assemblers.ArticleModelAssembler;
import blog.article.controllers.exceptions.ArticleNotFoundException;
import blog.article.domain.*;
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
        final ArticleCreateRequest article = TestData.testArticleRequestDTO();
        final ArticleEntity articleEntity = new ArticleEntity(article);
        articleEntity.setId(1L);
        final ArticleResponse articleResponse = new ArticleResponse(articleEntity);

        EntityModel<ArticleResponse> articleEntityModel = EntityModel.of(articleResponse,
                linkTo(methodOn(ArticleController.class).getArticle(articleResponse.id())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));

        // when - action or behaviour that we are going test
        when(service.saveArticle(any(ArticleCreateRequest.class))).thenReturn(articleResponse);
        when(assembler.toModel(any(ArticleResponse.class))).thenReturn(articleEntityModel);

        ResultActions response = mockMvc.perform(post("/v1/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(article)));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(articleResponse.title()))
                .andExpect(jsonPath("$.content").value(articleResponse.content()))
                .andExpect(jsonPath("$.tags[0].name").value(articleResponse.tags().get(0).getName()))
                .andExpect(jsonPath("$.publishDate").value(articleResponse.publishDate().toString()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.articles.href").exists());
    }

    // JUnit test for GET All articles REST API
    @Test
    void givenListOfArticles_whenGetAllArticles_thenReturnArticlesList() throws Exception {

        // given - precondition or setup
        final ArticleResponse article = TestData.testArticleResponseDTO();

        List<EntityModel<ArticleResponse>> listOfArticles = List.of(EntityModel.of(article));
        CollectionModel<EntityModel<ArticleResponse>> collectionModel = CollectionModel.of(listOfArticles);

        // when - action or the behaviour that we are going test
        when(service.getAllArticles()).thenReturn(List.of(article));
        when(assembler.toModel(any(ArticleResponse.class))).thenReturn(EntityModel.of(article));

        ResultActions response = mockMvc.perform(get("/v1/articles")
                .accept(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.articleResponseList[0].title").value(article.title()));
    }

    // positive scenario - valid Article id
    // JUnit test for GET Article by id REST API
    @Test
    void givenArticleId_whenGetArticleById_thenReturnArticleObject() throws Exception {

        // given - precondition or setup
        Long articleId = 1L;
        ArticleResponse article = TestData.testArticleResponseDTO();

        EntityModel<ArticleResponse> articleEntityModel = EntityModel.of(article,
                linkTo(methodOn(ArticleController.class).getArticle(article.id())).withSelfRel());

        // when - action or behaviour that we are going test
        when(service.getArticleById(articleId)).thenReturn(article);
        when(assembler.toModel(any(ArticleResponse.class))).thenReturn(articleEntityModel);

        ResultActions response = mockMvc.perform(get("/v1/articles/{id}", articleId)
                .accept(MediaType.APPLICATION_JSON));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(article.id().intValue())))
                .andExpect(jsonPath("$.title", is(article.title())))
                .andExpect(jsonPath("$.content", is(article.content())))
                .andExpect(jsonPath("$.tags[0].name", is(article.tags().get(0).getName())))
                .andExpect(jsonPath("$.publishDate", is(article.publishDate().toString())))
                .andExpect(jsonPath("$._links.self.href").exists());

    }

    // negative scenario - invalid article id
    // JUnit test for GET article by id REST API
    @Test
    void givenInvalidArticleId_whenGetArticleById_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        Long articleId = 99L;

        when(service.getArticleById(articleId)).thenThrow(new ArticleNotFoundException(articleId));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/v1/articles/{id}", articleId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for update article REST API - positive scenario
    @Test
    void givenUpdatedArticle_whenUpdateArticle_thenReturnUpdateArticleObject() throws Exception {

        // given - condition or setup
        Long articleId = 1L;
        ArticleUpdateRequest articleUpdateRequest = TestData.testArticleUpdateDTO();
        ArticleEntity articleEntity = new ArticleEntity(articleUpdateRequest);
        articleEntity.setId(articleId);
        ArticleResponse articleResponse = new ArticleResponse(articleEntity);

        EntityModel<ArticleResponse> articleEntityModel = EntityModel.of(articleResponse,
                linkTo(methodOn(ArticleController.class).getArticle(articleId)).withSelfRel(),
                linkTo(methodOn(ArticleController.class).getAllArticles()).withRel("articles"));

        given(service.updateArticle(eq(articleId), any(ArticleUpdateRequest.class))).willReturn(articleResponse);
        given(assembler.toModel(any(ArticleResponse.class))).willReturn(articleEntityModel);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/v1/articles/{id}", articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleResponse)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(articleId.intValue())))
                .andExpect(jsonPath("$.title", is(articleResponse.title())))
                .andExpect(jsonPath("$.content", is(articleResponse.content())))
                .andExpect(jsonPath("$.tags[0].name", is(articleResponse.tags().get(0).getName())))
                .andExpect(jsonPath("$.publishDate", is(articleResponse.publishDate().toString())))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.articles.href").exists());
    }

    // JUnit test for update article REST API - negative scenario
    @Test
    void givenUpdatedArticle_whenUpdateArticle_thenReturn404() throws Exception {

        // given - precondition or setup
        Long articleId = 1L;
        ArticleUpdateRequest updatedArticle = TestData.testArticleUpdateDTO();

        // when - action or the behaviour that we are going test
        when(service.updateArticle(eq(articleId), any(ArticleUpdateRequest.class))).thenThrow(new ArticleNotFoundException(articleId));

        ResultActions response = mockMvc.perform(put("/v1/articles/{id}", articleId)
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
        ResultActions response = mockMvc.perform(delete("/v1/articles/{id}", articleId));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    // JUnit test for GET articles by publish date REST API
    @Test
    void givenListOfArticlesByPublishDate_whenGetArticles_thenReturnArticlesList() throws Exception {

        // given - precondition or setup
        LocalDate publishDate = LocalDate.now();
        final ArticleResponse article = TestData.testArticleResponseDTO();

        List<EntityModel<ArticleResponse>> listOfArticles = List.of(EntityModel.of(article));
        CollectionModel<EntityModel<ArticleResponse>> collectionModel = CollectionModel.of(listOfArticles);

        // when - action or the behaviour that we are going test
        when(service.getArticlesByDate(publishDate)).thenReturn(List.of(article));
        when(assembler.toModel(any(ArticleResponse.class))).thenReturn(EntityModel.of(article));

        ResultActions response = mockMvc.perform(get("/v1/articles/filter").param("date", publishDate.toString())
                .accept(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.articleResponseList[0].title").value(article.title()));
    }

    // JUnit test for GET articles by tag name date REST API
    @Test
    void givenListOfArticles_whenGetArticlesByTagName_thenReturnArticlesList() throws Exception {

        // given - precondition or setup
        String tagName = "tag name";
        final ArticleResponse article = TestData.testArticleResponseDTO();

        List<EntityModel<ArticleResponse>> listOfArticles = List.of(EntityModel.of(article));
        CollectionModel<EntityModel<ArticleResponse>> collectionModel = CollectionModel.of(listOfArticles);

        // when - action or the behaviour that we are going test
        when(service.getArticlesByTagName(tagName)).thenReturn(List.of(article));
        when(assembler.toModel(any(ArticleResponse.class))).thenReturn(EntityModel.of(article));

        ResultActions response = mockMvc.perform(get("/v1/articles/tags/{tagName}", tagName)
                .accept(MediaType.APPLICATION_JSON));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.articleResponseList[0].title").value(article.title()));
    }
}