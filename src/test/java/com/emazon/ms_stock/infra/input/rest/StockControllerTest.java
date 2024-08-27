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
    void Should_ThrowsException_When_InvalidCategoryPayload() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(null).description(null).build());

        ResultActions res = sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors.description").value("must not be blank"));

        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name("l".repeat(51)).description("k".repeat(91)).build());

        res = sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value("size must be between 3 and 50"))
                .andExpect(jsonPath("$.fieldErrors.description").value("size must be between 3 and 90"));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllCategoriesPageable() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(validCategoryName).description(validCategoryDescription).build());
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_CATEGORIES_URL)));
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

    @Test
    void Should_ThrowsException_When_InvalidBrandPayload() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(null).description(null).build());

        ResultActions res = sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors.description").value("must not be blank"));

        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name("d").description("f").build());
        res = sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL);
        assertFieldErrors(res, 2);
        res.andExpect(jsonPath("$.fieldErrors.name").value("size must be between 3 and 50"))
                .andExpect(jsonPath("$.fieldErrors.description").value("size must be between 3 and 120"));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllBrandPageable() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name("Test").description("Test2").build());
        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL);

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_BRAND_URL)));
    }

    private void assertFieldErrors(ResultActions res, Integer numberOfFields) throws Exception {
        res.andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors", Matchers.aMapWithSize(numberOfFields)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void assertValidPageDTOFormResponseFromResult(ResultActions res) throws Exception {
        res.andExpect(jsonPath("$", Matchers.aMapWithSize(9)))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(20))
                .andExpect(jsonPath("$.currentPage").value(0));
    }

    private ResultActions sentPostToCreateEntity(String dto, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }
}