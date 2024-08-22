package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private String categoryString;

    @Test
    void testCorrectCreationOfCategory() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name("Test name").description("Descc").build());
        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(status().isCreated());
    }

    @Test
    void testAlreadyExistedCategory() throws Exception {
        categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name("Test name").description("Descc").build());
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
                .andExpect(jsonPath("$.fieldErrors.description").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.name").value("must not be null"))
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


    private ResultActions sentPostToCreateCategory(String dto) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/stock/categories")
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }
}