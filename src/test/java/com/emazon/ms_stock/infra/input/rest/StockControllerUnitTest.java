package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.application.dto.input.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.input.BrandReqDTO;
import com.emazon.ms_stock.application.dto.out.CategoryReqDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    /*
        Category
     */
    @Test
    void Should_CreateCategoryAndGetCorrectStatus_WhenValidPayload() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(CategoryReqDTO.builder().build()), ConsUtils.BASIC_CATEGORIES_URL).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void Should_ThrowsException_When_InvalidCategoryPayload() throws Exception {
        String categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(null).description(null).build());

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

    /*
        Brand
     */
    @Test
    void Should_CreateBrandAndGetCorrectStatus_WhenValidPayload() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(BrandReqDTO.builder().build()), ConsUtils.BASIC_CATEGORIES_URL).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void Should_ThrowsException_When_InvalidBrandPayload() throws Exception {
        String brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(null).description(null).build());

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


    /*
        * Article
    */
    @Test
    void Should_CreateArticleAndGetCorrectStatus_WhenValidPayload() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(ArticleReqDTO.builder().build()), ConsUtils.BASIC_CATEGORIES_URL).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    void Should_ThrowsException_When_InvalidArticlePayload() throws Exception {
        ResultActions res = sentPostToCreateEntity(mapper.writeValueAsString(ArticleReqDTO.builder().build()), ConsUtils.BASIC_ARTICLES_URL);

        assertFieldErrors(res, ConsUtils.FIELD_WITH_ERRORS_AT_ARTICLE);

        res.andExpect(jsonPath(ConsUtils.FIELD_NAME_PATH).value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath(ConsUtils.FIELD_DESCRIPTION_PATH).value(ExceptionResponse.NOT_BLANK))
                .andExpect(jsonPath(ConsUtils.FIELD_CATEGORY_IDS_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_PRICE_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_QUANTITY_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_BRAND_ID_PATH).value(ExceptionResponse.NOT_NULL));
    }

    /*** Supply ***/
    @Test
    @WithMockUser(roles = "AUX_DEPOT")
    void Should_ThrowsException_When_NotBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)).andExpect(status().isBadRequest());
    }

    @Test
    void Should_ThrowsException_When_NotValidAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void Should_ThrowsException_When_NotValidRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "AUX_DEPOT")
    void Should_ThrowsException_When_NotValidPayload() throws Exception {
        String supplyJSON = mapper.writeValueAsString(ItemsReqDTO.builder().items(Set.of(ItemQuantityDTO.builder().build())).build());

        sentPostToCreateEntity(supplyJSON, ConsUtils.SUPPLY_URL)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_QUANTITY_PATH_ARRAY).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_ARTICLE_ID_PATH_ARRAY).value(ExceptionResponse.NOT_NULL));
    }

    @Test
    @WithMockUser(roles = "AUX_DEPOT")
    void Should_ThrowsException_When_NoBody() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.builderPath().withArticles().withSupply().build()))
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.REQUIRED_BODY))
                .andExpect(status().isBadRequest());
    }

    /*** Cart Addition ***/
    @Test
    @WithMockUser(roles = ConsUtils.CLIENT)
    void Should_ThrowsException_When_NotValidFieldsForCart() throws Exception {
        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build())
                        .content(mapper.writeValueAsString(ItemsReqDTO.builder().build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.FIELD_VALIDATION_ERRORS))
                .andExpect(jsonPath(ConsUtils.FIELD_ITEMS).value(ExceptionResponse.NOT_NULL))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build())
                        .content(mapper.writeValueAsString(ItemsReqDTO.builder().items(Set.of(ItemQuantityDTO.builder().build())).build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.FIELD_VALIDATION_ERRORS))
                .andExpect(jsonPath(ConsUtils.FIELD_ARTICLE_ID_PATH_ARRAY).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_QUANTITY_PATH_ARRAY).value(ExceptionResponse.NOT_NULL))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = ConsUtils.CLIENT)
    void Should_ThrowsException_When_NotBodyForCart() throws Exception {
        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build()))
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.REQUIRED_BODY))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = ConsUtils.ADMIN)
    void Should_ThrowsException_When_NotValidRoleForCart() throws Exception {
        mockMvc.perform(put(ConsUtils.builderPath().withArticles().build(), ConsUtils.INTEGER_1))
                .andExpect(status().isForbidden());
    }

    @Test
    void Should_ThrowsException_When_NotAuthenticatedForCart() throws Exception {
        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build()))
                .andExpect(status().isUnauthorized());
    }

    /*** COMMON ***/
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