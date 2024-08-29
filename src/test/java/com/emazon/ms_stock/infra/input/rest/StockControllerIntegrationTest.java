package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception_handler.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StockControllerIntegrationTest {
    @SpyBean
    private ICategoryPersistencePort categoryPersistencePort;

    @SpyBean
    private IBrandPersistencePort brandPersistencePort;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

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

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllCategoriesPageable() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build());
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_CATEGORIES_URL)));
    }

    @Test
    void Should_ThrowsAndException_When_CategoryAlreadyExists() throws Exception {
        categoryJSON = mapper.writeValueAsString(CategoryReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build());
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Category already exists"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllBrandsPageable() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build());

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL).andExpect(status().isCreated());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_BRAND_URL)));
    }

    @Test
    void Should_ThrowsAnException_When_BrandAlreadyExists() throws Exception {
        brandJSON = mapper.writeValueAsString(BrandReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build());

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL).andExpect(status().isCreated());

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Brand already exists"))
                .andExpect(jsonPath("$.fieldErrors.name").value("must be unique"));
    }

    @Test
    void Should_ThrowsAnException_When_CategoryOrBrandIdsAreNotFoundForArticle() throws Exception {
        articleJSON = mapper.writeValueAsString(validArticle);

        sentPostToCreateEntity(articleJSON, BASIC_ARTICLES_URL)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error processing operation with: Article"))
                .andExpect(jsonPath("$.fieldErrors", Matchers.aMapWithSize(1)))
                .andExpect(jsonPath("$.fieldErrors.categoryIds").value(ExceptionResponse.ID_NOT_FOUND));

        Mockito.doReturn(List.of(new Category(TEST_NAME,VALID_DESC))).when(categoryPersistencePort).findAllById(Mockito.any());

        sentPostToCreateEntity(articleJSON, BASIC_ARTICLES_URL)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error processing operation with: Article"))
                .andExpect(jsonPath("$.fieldErrors", Matchers.aMapWithSize(1)))
                .andExpect(jsonPath("$.fieldErrors.brandId").value(ExceptionResponse.ID_NOT_FOUND));

    }

    @Test
    void Should_ThrowsAnException_When_ArticleAlreadyExists() throws Exception {
        articleJSON = mapper.writeValueAsString(validArticle);

        Mockito.doReturn(List.of(new Category(TEST_NAME,VALID_DESC))).when(categoryPersistencePort).findAllById(Mockito.any());
        Mockito.doReturn(Optional.of(new Brand(TEST_NAME,VALID_DESC))).when(brandPersistencePort).findById(Mockito.any());

        sentPostToCreateEntity(articleJSON, BASIC_ARTICLES_URL)
                .andExpect(status().isCreated());
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
