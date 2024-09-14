package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.BrandReqDTO;
import com.emazon.ms_stock.application.dto.CategoryReqDTO;
import com.emazon.ms_stock.application.dto.supply.SupplyReqDTO;
import com.emazon.ms_stock.application.handler.IStockHandler;
import com.emazon.ms_stock.application.mapper.ArticleDTOMapper;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.infra.exception_handler.ExceptionResponse;
import com.emazon.ms_stock.infra.security.model.CustomUserDetails;
import com.emazon.ms_stock.infra.security.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@WithMockUser(roles="ADMIN")
class StockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ArticleDTOMapper articleDTOMapper;

    @SpyBean
    private IStockHandler stockHandler;

    @SpyBean
    private IArticlePersistencePort articlePersistencePort;

    private String categoryJSON;
    private String brandJSON;

    private static final String USER = "testUser";
    private static final String AUX_DEPOT = "AUX_DEPOT";
    private static final String CLIENT = "CLIENT";
    private static final Long USER_ID = 1L;
    private static final Long ARTICLE_ID = 1L;
    private static final String PASSWORD = "password";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String BASIC_CATEGORIES_URL = "/stock/categories";
    private static final String BASIC_BRAND_URL = "/stock/brands";
    private static final String BASIC_ARTICLES_URL = "/stock/articles";

    private static final String TEST_NAME = "Test name";
    private static final String LAST_TEST_NAME = "Zipikaka";
    private static final String VALID_DESC = "description";
    private static final String BASE_MATCHER = "$";
    private static final String NAME_OF_FIRST_CATEGORY_ON_ARTICLE = "$.content[0].categories[0].name";

    private final ArticleReqDTO articleReqDTO = ArticleReqDTO.builder()
            .name(TEST_NAME)
            .description(VALID_DESC)
            .price(BigDecimal.TEN)
            .quantity(ConsUtils.LONG_ONE)
            .categoryIds(Set.of(ConsUtils.LONG_ONE))
            .brandId(ConsUtils.LONG_ONE)
            .build();
    private final BrandReqDTO brandReqDTO = BrandReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build();
    private final CategoryReqDTO categoryReqDTO = CategoryReqDTO.builder().name(TEST_NAME).description(VALID_DESC).build();
    private final CategoryReqDTO categoryReqDTO2 = CategoryReqDTO.builder().name(LAST_TEST_NAME).description(VALID_DESC).build();
    private final SupplyReqDTO supplyReqDTO = SupplyReqDTO.builder().articleId(ARTICLE_ID).quantity(ConsUtils.LONG_ONE).build();

    /*** CATEGORY ***/

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldCategory() throws Exception{
        getAllPageableEntities(BASIC_CATEGORIES_URL, buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_CategoryAlreadyExists() throws Exception {
        categoryJSON = mapper.writeValueAsString(categoryReqDTO);
        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL);

        sentPostToCreateEntity(categoryJSON, BASIC_CATEGORIES_URL)
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(Category.class.getSimpleName() + ExceptionResponse.ENTITY_ALREADY_EXISTS));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllCategoriesPageable() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), BASIC_CATEGORIES_URL);

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_CATEGORIES_URL)), ConsUtils.INTEGER_1);
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
        brandJSON = mapper.writeValueAsString(brandReqDTO);
        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL);

        sentPostToCreateEntity(brandJSON, BASIC_BRAND_URL)
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(Brand.class.getSimpleName() + ExceptionResponse.ENTITY_ALREADY_EXISTS));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllBrandsPageable() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), BASIC_BRAND_URL).andExpect(status().isCreated());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_BRAND_URL)), ConsUtils.INTEGER_1);
    }

    /*** ARTICLES ***/

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldArticles() throws Exception{
        getAllPageableEntities(BASIC_ARTICLES_URL, buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_CategoryOrBrandDoesNotExists() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), BASIC_ARTICLES_URL)
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.ERROR_PROCESSING_OPERATION + Article.class.getSimpleName()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetCorrectObjectFromPageResponse_When_GetAllArticlesPageable() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), BASIC_CATEGORIES_URL);
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), BASIC_BRAND_URL).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), BASIC_ARTICLES_URL).andExpect(status().isCreated());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(BASIC_ARTICLES_URL)), ConsUtils.INTEGER_1);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetCorrectObjectFromPageResponse_When_GetAllArticlesCategoryAscAndDesc() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), BASIC_CATEGORIES_URL).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO2), BASIC_CATEGORIES_URL).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), BASIC_BRAND_URL).andExpect(status().isCreated());

        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), BASIC_ARTICLES_URL).andExpect(status().isCreated());
        articleReqDTO.setCategoryIds(Set.of(ConsUtils.LONG_TWO));
        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), BASIC_ARTICLES_URL).andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get(BASIC_ARTICLES_URL)
                        .param(ConsUtils.COLUMN_PARAM, ConsUtils.SORT_CATEGORY_NAME)
                        .param(ConsUtils.DIRECTION_PARAM, ConsUtils.SORT_ASC_VALUE))
                    .andExpect(jsonPath(ConsUtils.FIELD_CONTENT, Matchers.iterableWithSize(ConsUtils.INTEGER_2)))
                    .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.INTEGER_2))
                    .andExpect(jsonPath(NAME_OF_FIRST_CATEGORY_ON_ARTICLE).value(TEST_NAME));

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get(BASIC_ARTICLES_URL)
                        .param(ConsUtils.COLUMN_PARAM, ConsUtils.SORT_CATEGORY_NAME)
                        .param(ConsUtils.DIRECTION_PARAM, ConsUtils.SORT_DESC_VALUE))
                .andExpect(jsonPath(ConsUtils.FIELD_CONTENT, Matchers.iterableWithSize(ConsUtils.INTEGER_2)))
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.INTEGER_2))
                .andExpect(jsonPath(NAME_OF_FIRST_CATEGORY_ON_ARTICLE).value(LAST_TEST_NAME));

        assertValidPageDTOFormResponseFromResult(res, ConsUtils.INTEGER_2);
    }

    /*** Supply + Token Security ***/

    @Test
    void Should_ThrowsException_When_InvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)
                    .header(AUTHORIZATION, BEARER + " "))
                    .andExpect(status().isUnauthorized());
    }

    @Test
    void Should_ThrowsException_When_NotValidRoleOnToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)
                    .header(AUTHORIZATION, BEARER + getClientToken()))
                    .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetOK_When_ValidScenario() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), BASIC_CATEGORIES_URL);
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), BASIC_BRAND_URL).andExpect(status().isCreated());

        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), BASIC_ARTICLES_URL).andExpect(status().isCreated());

        postSupplyWithAuxDepotToken().andExpect(status().isCreated());

        Mockito.verify(articlePersistencePort, Mockito.times(1)).saveAll(Mockito.any());
    }

    @Test
    void Should_GetOK_When_NotValidArticlesOnStock() throws Exception {
        Mockito.doReturn(List.of()).when(articlePersistencePort).findAllById(Mockito.any());

        postSupplyWithAuxDepotToken()
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.ERROR_PROCESSING_OPERATION + Article.class.getSimpleName()))
            .andExpect(jsonPath(ConsUtils.FIELD_ID_PATH).value(ExceptionResponse.ID_NOT_FOUND));

        Mockito.verify(articlePersistencePort, Mockito.times(0)).saveAll(Mockito.any());
    }

    /*
     * Common
     */

    private ResultActions postSupplyWithAuxDepotToken() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(Set.of(supplyReqDTO)))
                .header(AUTHORIZATION, BEARER + getAuxDepotToken()));
    }

    @Test
    void Should_ThrowsAnException_When_InvalidSortDirection() throws Exception {
        getAllPageableEntities(BASIC_BRAND_URL, buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.NON_EXISTING_DIRECTION_COLUMN)))
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

    private void assertValidPageDTOFormResponseFromResult(ResultActions res, Integer elements) throws Exception {
        final int ELEMENTS_FOR_PAGE = 9;
        res.andExpect(jsonPath(BASE_MATCHER, Matchers.aMapWithSize(ELEMENTS_FOR_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(elements))
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_PAGES).value(ConsUtils.INTEGER_1))
                .andExpect(jsonPath(ConsUtils.FIELD_PAGEABLE_PAGE_SIZE).value(20))
                .andExpect(jsonPath(ConsUtils.FIELD_CURRENT_PAGE).value(0));
    }

    private ResultActions sentPostToCreateEntity(String dto, String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(dto)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private String getAuxDepotToken() {
        CustomUserDetails userDetail = new CustomUserDetails(USER, PASSWORD, Set.of(new SimpleGrantedAuthority("ROLE_".concat(AUX_DEPOT))), USER_ID);
        return JwtUtils.createToken(new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));
    }

    private String getClientToken() {
        CustomUserDetails userDetail = new CustomUserDetails(USER, PASSWORD, Set.of(new SimpleGrantedAuthority("ROLE_".concat(CLIENT))), USER_ID);
        return JwtUtils.createToken(new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));
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
