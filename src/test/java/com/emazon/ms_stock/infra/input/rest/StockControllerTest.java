package com.emazon.ms_stock.infra.input.rest;

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
    private String categoryString;
    private static final String BASIC_URL = "/stock/categories";
    private final String validCategoryName = "Test name";
    private final String validCategoryDescription = "description";

    @Test
    void testCorrectCreationOfCategory() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name(validCategoryName).description(validCategoryDescription).build());
        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(status().isCreated());
    }

    @Test
    void testAlreadyExistedCategory() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name(validCategoryName).description(validCategoryDescription).build());
        sentPostToCreateCategory(categoryString);

        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Category already exists"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testNotValidNullFields() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name(null).description(null).build());
        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors", Matchers.aMapWithSize(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNotValidBlankFields() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name("   ").description("   ").build());
        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors.description").value("must not be blank"))
                .andExpect(jsonPath("$.fieldErrors.name").value("must not be blank"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNotValidLengthFields() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder()
                .name("l".repeat(51))
                .description("k".repeat(91))
                .build());
        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(jsonPath("$.fieldErrors.description").value("size must be between 3 and 90"))
                .andExpect(jsonPath("$.fieldErrors.name").value("size must be between 3 and 50"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPageStructureOfAllCategoriesResponse() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name(validCategoryName).description(validCategoryDescription).build());
        sentPostToCreateCategory(categoryString);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get(BASIC_URL));

        res.andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(20));
    }

    private ResultActions sentPostToCreateCategory(String dto) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(BASIC_URL)
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }
}