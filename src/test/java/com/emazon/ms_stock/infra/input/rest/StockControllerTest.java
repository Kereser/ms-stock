package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    private CategoryReqDTO categoryReqDTO;

    @BeforeEach
    void init() {
        categoryReqDTO = CategoryReqDTO.builder().name("Test name").description("Test valid desc").build();
    }

    @Test
    void testCorrectCreationOfCategory() throws Exception {
        String categoryString = mapper.writeValueAsString(categoryReqDTO);

        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(status().isCreated());
    }

    @Test
    void testNotValidFields() throws Exception {
        String categoryString = mapper.writeValueAsString(CategoryReqDTO.builder().name(null).description("").build());

        ResultActions res = sentPostToCreateCategory(categoryString);

        res.andExpect(jsonPath("$.message").value("Request has field validation errors"))
                .andExpect(status().isBadRequest());
    }

    private ResultActions sentPostToCreateCategory(String dto) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/stock/categories")
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }
}