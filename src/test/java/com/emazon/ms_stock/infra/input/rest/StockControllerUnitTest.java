package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.handler.StockHandler;
import com.emazon.ms_stock.infra.exception_handler.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StockControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private StockHandler stockHandler;

    private String categoryJSON;
    private String brandJSON;
    private String articleJSON;

    private static final String BASIC_CATEGORIES_URL = "/stock/categories";
    private static final String BASIC_BRAND_URL = "/stock/brands";
    private static final String BASIC_ARTICLES_URL = "/stock/articles";

    private static final String TEST_NAME = "Test name";
    private static final String VALID_DESC = "description";

    private final ArticleReqDTO validArticle = ArticleReqDTO.builder()
            .name(TEST_NAME)
            .description(VALID_DESC)
            .price(BigDecimal.TEN)
            .quantity(1L)
            .categoryIds(Set.of(1L))
            .brandId(1L)
            .build();

    /*
        Category
     */
    @Test
    void Should_CreateCategoryAndGetCorrectStatus_WhenValidPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build());
        ResultActions res = sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        res.andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsException_When_InvalidCategoryPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(null).description(null).build());

        ResultActions res = sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath("$.fieldErrors.description").value(ExceptionResponse.NOT_BLANK));

        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name("l".repeat(51)).description("k".repeat(91)).build());

        res = sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value(ExceptionResponse.SIZE_BETWEEN_3_50))
            .andExpect(jsonPath("$.fieldErrors.description").value(ExceptionResponse.SIZE_BETWEEN_3_90));
    }

    /*
        Brand
     */
    @Test
    void Should_CreateBrandAndGetCorrectStatus_WhenValidPayload() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build());

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsException_When_InvalidBrandPayload() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(null).description(null).build());

        ResultActions res = sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath("$.fieldErrors.description").value(ExceptionResponse.NOT_BLANK));

        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name("d").description("f").build());
        res = sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value(ExceptionResponse.SIZE_BETWEEN_3_50))
                .andExpect(jsonPath("$.fieldErrors.description").value(ExceptionResponse.SIZE_BETWEEN_3_120));
    }

    /*
        * Article
    */
    @Test
    void Should_CreateArticleAndGetCorrectStatus_WhenValidPayload() throws Exception {
        articleJSON = mapper.writeValueAsString(validArticle);

        sentPostToCreateEntity(articleJSON, BASIC_ARTICLES_URL).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsException_When_InvalidArticlePayload() throws Exception {
        articleJSON = mapper.writeValueAsString(ArticleReqDTO.builder().build());

        ResultActions res = sentPostToCreateEntity(articleJSON, BASIC_ARTICLES_URL);
        assertFieldErrors(res, 6);
        res.andExpect(jsonPath("$.fieldErrors.name").value(ExceptionResponse.NOT_BLANK))
            .andExpect(jsonPath("$.fieldErrors.description").value(ExceptionResponse.NOT_BLANK))
            .andExpect(jsonPath("$.fieldErrors.categoryIds").value(ExceptionResponse.NOT_NULL))
            .andExpect(jsonPath("$.fieldErrors.price").value(ExceptionResponse.NOT_NULL))
            .andExpect(jsonPath("$.fieldErrors.quantity").value(ExceptionResponse.NOT_NULL))
            .andExpect(jsonPath("$.fieldErrors.brandId").value(ExceptionResponse.NOT_NULL));
    }


    private void assertFieldErrors(ResultActions res, Integer numberOfFields) throws Exception {
        res.andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors", Matchers.aMapWithSize(numberOfFields)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private ResultActions sentPostToCreateEntity(String dto, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }
}