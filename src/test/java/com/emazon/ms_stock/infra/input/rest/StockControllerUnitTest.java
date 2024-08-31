package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.ConsUtils;
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

    private final ArticleReqDTO validArticle = ArticleReqDTO.builder()
            .name(ConsUtils.TEST_NAME)
            .description(ConsUtils.DESC_NAME)
            .price(BigDecimal.TEN)
            .quantity(ConsUtils.LONG_ONE)
            .categoryIds(Set.of(ConsUtils.LONG_ONE))
            .brandId(ConsUtils.LONG_ONE)
            .build();

    /*
        Category
     */
    @Test
    void Should_CreateCategoryAndGetCorrectStatus_WhenValidPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(ConsUtils.TEST_NAME).description(ConsUtils.DESC_NAME).build());
        ResultActions res = sentPostToCreateEntity(categoryJSON, ConsUtils.BASIC_CATEGORIES_URL);

        res.andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsException_When_InvalidCategoryPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(null).description(null).build());

        ResultActions res = sentPostToCreateEntity(categoryJSON, ConsUtils.BASIC_CATEGORIES_URL);
        assertFieldErrors(res, ConsUtils.FIELD_WITH_ERRORS_AT_CATEGORY);
        res.andExpect(jsonPath(ConsUtils.FIELD_NAME_PATH).value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath(ConsUtils.FIELD_DESCRIPTION_PATH).value(ExceptionResponse.NOT_BLANK));

        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(ConsUtils.PLUS_FIFTY_CHARACTERS).description(ConsUtils.PLUS_NINETY_CHARACTERS).build());

        res = sentPostToCreateEntity(categoryJSON, ConsUtils.BASIC_CATEGORIES_URL);
        assertFieldErrors(res, ConsUtils.FIELD_WITH_ERRORS_AT_CATEGORY);
        res.andExpect(jsonPath(ConsUtils.FIELD_NAME_PATH).value(ExceptionResponse.SIZE_BETWEEN_3_50))
            .andExpect(jsonPath(ConsUtils.FIELD_DESCRIPTION_PATH).value(ExceptionResponse.SIZE_BETWEEN_3_90));
    }

    @Test
    void Should_Get200Code_When_GetAllCategories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.BASIC_CATEGORIES_URL)).andExpect(status().isOk());
    }

    /*
        Brand
     */
    @Test
    void Should_CreateBrandAndGetCorrectStatus_WhenValidPayload() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(ConsUtils.TEST_NAME).description(ConsUtils.DESC_NAME).build());

        sentPostToCreateEntity(brandJSON, ConsUtils.BASIC_BRAND_URL).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsException_When_InvalidBrandPayload() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(null).description(null).build());

        ResultActions res = sentPostToCreateEntity(brandJSON, ConsUtils.BASIC_BRAND_URL);
        assertFieldErrors(res, ConsUtils.FIELD_WITH_ERRORS_AT_BRAND);
        res.andExpect(jsonPath(ConsUtils.FIELD_NAME_PATH).value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath(ConsUtils.FIELD_DESCRIPTION_PATH).value(ExceptionResponse.NOT_BLANK));

        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(ConsUtils.PLUS_FIFTY_CHARACTERS).description(ConsUtils.PLUS_TWO_HUNDRED_CHARACTERS).build());
        res = sentPostToCreateEntity(brandJSON, ConsUtils.BASIC_BRAND_URL);
        assertFieldErrors(res, ConsUtils.FIELD_WITH_ERRORS_AT_BRAND);
        res.andExpect(jsonPath(ConsUtils.FIELD_NAME_PATH).value(ExceptionResponse.SIZE_BETWEEN_3_50))
                .andExpect(jsonPath(ConsUtils.FIELD_DESCRIPTION_PATH).value(ExceptionResponse.SIZE_BETWEEN_3_120));
    }

    @Test
    void Should_Get200Code_When_GetAllBrands() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.BASIC_BRAND_URL)).andExpect(status().isOk());
    }

    /*
        * Article
    */
    @Test
    void Should_CreateArticleAndGetCorrectStatus_WhenValidPayload() throws Exception {
        articleJSON = mapper.writeValueAsString(validArticle);

        sentPostToCreateEntity(articleJSON, ConsUtils.BASIC_ARTICLES_URL).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsException_When_InvalidArticlePayload() throws Exception {
        articleJSON = mapper.writeValueAsString(ArticleReqDTO.builder().build());

        ResultActions res = sentPostToCreateEntity(articleJSON, ConsUtils.BASIC_ARTICLES_URL);
        assertFieldErrors(res, ConsUtils.FIELD_WITH_ERRORS_AT_ARTICLE);
        res.andExpect(jsonPath(ConsUtils.FIELD_NAME_PATH).value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath(ConsUtils.FIELD_DESCRIPTION_PATH).value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath(ConsUtils.FIELD_CATEGORYIDS_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_PRICE_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_QUANTITY_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_BRANDID_PATH).value(ExceptionResponse.NOT_NULL));
    }

    @Test
    void Should_Get200Code_When_GetAllArticles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.BASIC_ARTICLES_URL)).andExpect(status().isOk());
    }

    private void assertFieldErrors(ResultActions res, Integer numberOfFields) throws Exception {
        final String fieldErrors = ConsUtils.FIELD_ERROR;
        res.andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.FIELD_VALIDATION_ERRORS))
                .andExpect(jsonPath(fieldErrors).isMap())
                .andExpect(jsonPath(fieldErrors, Matchers.aMapWithSize(numberOfFields)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private ResultActions sentPostToCreateEntity(String dto, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }
}