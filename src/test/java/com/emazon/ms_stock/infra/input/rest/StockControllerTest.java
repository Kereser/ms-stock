package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private String categoryJSON;
    private static final String BASIC_CATEGORIES_URL = "/stock/categories";
    private static final String BASIC_BRAND_URL = "/stock/brands";
    private final String validCategoryName = "Test name";
    private final String validCategoryDescription = "description";
    private String brandJSON;

    /*
        Category
     */
    @Test
    void Should_CreateCategoryAndGetCorrectStatus_WhenValidPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(validCategoryName).description(validCategoryDescription).build());
        ResultActions res = sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        res.andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsAndException_When_CategoryAlreadyExists() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(validCategoryName).description(validCategoryDescription).build());
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Category already exists"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void Should_GetFieldErrors_When_NullFieldsInPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(null).description(null).build());

        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL)
                .andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors", Matchers.aMapWithSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_GetFieldErrors_When_BlankFieldsInPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name("   ").description("   ").build());

        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL)
                .andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors.description").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors.name").value("must not be blank"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_GetFieldErrors_When_InvalidLengthFieldsInPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder()
                .name("l".repeat(51))
                .description("k".repeat(91))
                .build());

        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL)
                .andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors.description").value("size must be between 3 and 90"))
                .andExpect(jsonPath("$.fieldErrors.name").value("size must be between 3 and 50"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllCategoriesPageable() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(validCategoryName).description(validCategoryDescription).build());
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        mockMvc.perform(MockMvcRequestBuilders.get(BASIC_CATEGORIES_URL))
                .andExpect(jsonPath("$", Matchers.aMapWithSize(4)))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(20));
    }

    private ResultActions sentPostToCreateEntity(String dto, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }

    /*
        Brand
     */
    @Test
    void Should_SaveBrand_When_UniqueName() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name("Test name").description("Desc").build());

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsAnException_When_BrandAlreadyExists() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name("Test name").description("Desc").build());

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL).andExpect(status().isCreated());

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Brand already exists"))
                .andExpect(jsonPath("$.fieldErrors.name").value("must be unique"));
    }
}