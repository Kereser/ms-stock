package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.infra.exception_handler.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

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
            .quantity(ConsUtils.LONG_ONE)
            .categoryIds(Set.of(ConsUtils.LONG_ONE))
            .brandId(ConsUtils.LONG_ONE)
            .build();

    private final BrandReqDTO validBrand = BrandReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build();
    private final CategoryReqDTO validCategory = CategoryReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build();

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldCategory() throws Exception{
        getAllPageableEntities(BASIC_CATEGORIES_URL, buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_CategoryAlreadyExists() throws Exception {
        categoryJSON = mapper.writeValueAsString(validCategory);
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL)
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(Category.class.getSimpleName() + ExceptionResponse.ENTITY_ALREADY_EXISTS));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllCategoriesPageable() throws Exception {
        categoryJSON = mapper.writeValueAsString(validCategory);
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_CATEGORIES_URL)));
    }

    /**** BRAND ****/

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldBrand() throws Exception{
        getAllPageableEntities(BASIC_BRAND_URL, buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_BrandAlreadyExists() throws Exception {
        brandJSON = mapper.writeValueAsString(validBrand);
        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL);

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL)
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(Brand.class.getSimpleName() + ExceptionResponse.ENTITY_ALREADY_EXISTS));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllBrandsPageable() throws Exception {
        brandJSON = mapper.writeValueAsString(validBrand);
        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL).andExpect(status().isCreated());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_BRAND_URL)));
    }

    /*** ARTICLES ***/

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldArticles() throws Exception{
        getAllPageableEntities(BASIC_ARTICLES_URL, buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_CategoryOrBrandDoesntExists() throws Exception {
        articleJSON = mapper.writeValueAsString(validArticle);

        sentPostToCreateEntity(articleJSON, BASIC_ARTICLES_URL)
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.ERROR_PROCESSING_OPERATION + Article.class.getSimpleName()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetCorrectObjectFromPageResponse_When_GetAllArticlesPageable() throws Exception {
        categoryJSON = mapper.writeValueAsString(validCategory);
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);
        brandJSON = mapper.writeValueAsString(validBrand);
        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL).andExpect(status().isCreated());

        entityManager.flush();

        articleJSON = mapper.writeValueAsString(validArticle);

        sentPostToCreateEntity(articleJSON, BASIC_ARTICLES_URL)
                .andExpect(status().isCreated());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_ARTICLES_URL)));
    }

    /*
     * Common
     */
    @Test
    void Should_ThrowsAnException_When_InvalidSortDirection() throws Exception {
        getAllPageableEntities(BASIC_ARTICLES_URL, buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.NON_EXISTING_DIRECTION_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.NOT_VALID_PARAM))
                .andExpect(jsonPath(ConsUtils.FIELD_ERROR, Matchers.aMapWithSize(1)))
                .andExpect(jsonPath(ConsUtils.FIELD_DIRECTION_PATH).value(ConsUtils.NON_EXISTING_DIRECTION_COLUMN));

        getAllPageableEntities(BASIC_CATEGORIES_URL, buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.NON_EXISTING_DIRECTION_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.NOT_VALID_PARAM))
                .andExpect(jsonPath(ConsUtils.FIELD_ERROR, Matchers.aMapWithSize(1)))
                .andExpect(jsonPath(ConsUtils.FIELD_DIRECTION_PATH).value(ConsUtils.NON_EXISTING_DIRECTION_COLUMN));

        getAllPageableEntities(BASIC_ARTICLES_URL, buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.NON_EXISTING_DIRECTION_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.NOT_VALID_PARAM))
                .andExpect(jsonPath(ConsUtils.FIELD_ERROR, Matchers.aMapWithSize(1)))
                .andExpect(jsonPath(ConsUtils.FIELD_DIRECTION_PATH).value(ConsUtils.NON_EXISTING_DIRECTION_COLUMN));
    }

    private void assertValidPageDTOFormResponseFromResult(ResultActions res) throws Exception {
        res.andExpect(jsonPath("$", Matchers.aMapWithSize(9)))
                .andExpect(status().isOk())
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

    private ResultActions getAllPageableEntities(String url, MultiValueMap<String, String> params) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url).params(params)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    private MultiValueMap<String, String> buildParams(Map<String, String> params) {
        LinkedMultiValueMap<String, String> res = new LinkedMultiValueMap<>();

        params.forEach(res::add);
        return res;
    }

    private MultiValueMap<String, String> buildNonValidSortColumnParam() {
        return buildParams(Map.of(ConsUtils.COLUMN_PARAM, ConsUtils.NON_EXISTING_SORTING_COLUMN));
    }
}
