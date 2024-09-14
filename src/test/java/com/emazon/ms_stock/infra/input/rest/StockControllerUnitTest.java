package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.supply.SupplyReqDTO;
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
                .andExpect(jsonPath(ConsUtils.FIELD_CATEGORYIDS_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_PRICE_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_QUANTITY_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_BRANDID_PATH).value(ExceptionResponse.NOT_NULL));
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
        String supplyJSON = mapper.writeValueAsString(Set.of(SupplyReqDTO.builder().build()));

        sentPostToCreateEntity(supplyJSON, ConsUtils.SUPPLY_URL)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_QUANTITY_PATH).value(ExceptionResponse.NOT_NULL))
                .andExpect(jsonPath(ConsUtils.FIELD_ARTICLE_ID_PATH).value(ExceptionResponse.NOT_NULL));
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