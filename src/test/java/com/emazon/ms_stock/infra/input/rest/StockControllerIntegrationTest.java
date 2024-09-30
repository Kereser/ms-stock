package com.emazon.ms_stock.infra.input.rest;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.*;
import com.emazon.ms_stock.application.dto.input.ArticleReqDTO;
import com.emazon.ms_stock.application.dto.input.BrandReqDTO;
import com.emazon.ms_stock.application.dto.out.CategoryReqDTO;
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
import jakarta.persistence.PersistenceContext;
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
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@WithMockUser(roles="ADMIN")
class StockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ArticleDTOMapper articleDTOMapper;

    @SpyBean
    private IStockHandler stockHandler;

    @SpyBean
    private IArticlePersistencePort articlePersistencePort;

    private final ArticleReqDTO articleReqDTO = ArticleReqDTO.builder()
            .name(ConsUtils.TEST_NAME)
            .description(ConsUtils.VALID_DESC)
            .price(BigDecimal.TEN)
            .quantity(ConsUtils.LONG_1)
            .categoryIds(Set.of(ConsUtils.LONG_1))
            .brandId(ConsUtils.LONG_1)
            .build();
    private final BrandReqDTO brandReqDTO = BrandReqDTO.builder().name(ConsUtils.TEST_NAME).description(ConsUtils.VALID_DESC).build();
    private final CategoryReqDTO categoryReqDTO = CategoryReqDTO.builder().name(ConsUtils.TEST_NAME).description(ConsUtils.VALID_DESC).build();
    private final CategoryReqDTO categoryReqDTO2 = CategoryReqDTO.builder().name(ConsUtils.LAST_TEST_NAME).description(ConsUtils.VALID_DESC).build();
    private static final ItemQuantityDTO ITEM_QUANTITY_DTO = ItemQuantityDTO.builder().articleId(ConsUtils.LONG_1).quantity(ConsUtils.LONG_1).build();
    private static final ItemQuantityDTO ITEM_QUANTITY_DTO_2 = ItemQuantityDTO.builder().articleId(ConsUtils.LONG_2).quantity(ConsUtils.LONG_2).build();
    private final ItemsReqDTO itemsReqDTO = ItemsReqDTO.builder().items(Set.of(ITEM_QUANTITY_DTO)).build();

    /*** CATEGORY ***/
    @Test
    void Should_Get201OnBrandCategory_When_ValidPayload() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), ConsUtils.builderPath().withCategories().build()).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldCategory() throws Exception{
        getAllPageableEntities(ConsUtils.builderPath().withCategories().build(), buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_CategoryAlreadyExists() throws Exception {
        String categoryJSON = mapper.writeValueAsString(categoryReqDTO);
        sentPostToCreateEntity(categoryJSON, ConsUtils.builderPath().withCategories().build()).andExpect(status().isCreated());

        sentPostToCreateEntity(categoryJSON, ConsUtils.builderPath().withCategories().build())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(Category.class.getSimpleName() + ExceptionResponse.ENTITY_ALREADY_EXISTS));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllCategoriesPageable() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), ConsUtils.builderPath().withCategories().build());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.builderPath().withCategories().build())), ConsUtils.INTEGER_1);
    }

    /**** BRAND ****/
    @Test
    void Should_Get201OnBrandCreation_When_ValidPayload() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), ConsUtils.builderPath().withBrands().build()).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldBrand() throws Exception{
        getAllPageableEntities(ConsUtils.builderPath().withBrands().build(), buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_BrandAlreadyExists() throws Exception {
        String brandJSON = mapper.writeValueAsString(brandReqDTO);
        sentPostToCreateEntity(brandJSON, ConsUtils.builderPath().withBrands().build());

        sentPostToCreateEntity(brandJSON, ConsUtils.builderPath().withBrands().build())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(Brand.class.getSimpleName() + ExceptionResponse.ENTITY_ALREADY_EXISTS));
    }

    @Test
    void Should_GetCorrectObjectFromPageResponse_When_GetAllBrandsPageable() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), ConsUtils.builderPath().withBrands().build()).andExpect(status().isCreated());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.builderPath().withBrands().build())), ConsUtils.INTEGER_1);
    }

    /*** ARTICLES ***/
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_Get201OnArticleCreation_When_ValidPayload() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), ConsUtils.builderPath().withCategories().build()).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), ConsUtils.builderPath().withBrands().build()).andExpect(status().isCreated());

        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), ConsUtils.builderPath().withArticles().build()).andExpect(status().isCreated());
    }

    @Test
    void Should_ThrowsAnException_When_InvalidSortFieldArticles() throws Exception{
        getAllPageableEntities(ConsUtils.builderPath().withArticles().build(), buildNonValidSortColumnParam())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ConsUtils.INVALID_SORT_CRITERIA));
    }

    @Test
    void Should_ThrowsException_When_CategoryOrBrandDoesNotExists() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), ConsUtils.builderPath().withArticles().build())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.ERROR_PROCESSING_OPERATION + Article.class.getSimpleName()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetCorrectObjectFromPageResponse_When_GetAllArticlesPageable() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), ConsUtils.builderPath().withCategories().build());
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), ConsUtils.builderPath().withBrands().build()).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), ConsUtils.builderPath().withArticles().build()).andExpect(status().isCreated());

        assertValidPageDTOFormResponseFromResult(mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.builderPath().withArticles().build())), ConsUtils.INTEGER_1);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetCorrectObjectFromPageResponse_When_GetAllArticlesCategoryAscAndDesc() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), ConsUtils.builderPath().withCategories().build()).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO2), ConsUtils.builderPath().withCategories().build()).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), ConsUtils.builderPath().withBrands().build()).andExpect(status().isCreated());

        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), ConsUtils.builderPath().withArticles().build()).andExpect(status().isCreated());
        articleReqDTO.setCategoryIds(Set.of(ConsUtils.LONG_2));
        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), ConsUtils.builderPath().withArticles().build()).andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.builderPath().withArticles().build())
                        .param(ConsUtils.COLUMN_PARAM, ConsUtils.CATEGORY_PARAM_VALUE)
                        .param(ConsUtils.DIRECTION_PARAM, ConsUtils.SORT_ASC_VALUE))
                    .andExpect(jsonPath(ConsUtils.FIELD_CONTENT, Matchers.iterableWithSize(ConsUtils.INTEGER_2)))
                    .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.INTEGER_2))
                    .andExpect(jsonPath(ConsUtils.NAME_OF_FIRST_CATEGORY_ON_ARTICLE).value(ConsUtils.TEST_NAME));

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get(ConsUtils.builderPath().withArticles().build())
                        .param(ConsUtils.COLUMN_PARAM, ConsUtils.CATEGORY_PARAM_VALUE)
                        .param(ConsUtils.DIRECTION_PARAM, ConsUtils.SORT_DESC_VALUE))
                .andExpect(jsonPath(ConsUtils.FIELD_CONTENT, Matchers.iterableWithSize(ConsUtils.INTEGER_2)))
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.INTEGER_2))
                .andExpect(jsonPath(ConsUtils.NAME_OF_FIRST_CATEGORY_ON_ARTICLE).value(ConsUtils.LAST_TEST_NAME));

        assertValidPageDTOFormResponseFromResult(res, ConsUtils.INTEGER_2);
    }

    /*** Supply + Token Security ***/
    @Test
    void Should_ThrowsException_When_InvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)
                    .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + " "))
                    .andExpect(status().isUnauthorized());
    }

    @Test
    void Should_ThrowsException_When_NotValidRoleOnToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)
                    .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                    .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetOK_When_ValidScenario() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), ConsUtils.builderPath().withCategories().build());
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), ConsUtils.builderPath().withBrands().build()).andExpect(status().isCreated());

        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), ConsUtils.builderPath().withArticles().build()).andExpect(status().isCreated());

        postSupplyWithAuxDepotToken().andExpect(status().isCreated());

        Mockito.verify(articlePersistencePort, Mockito.times(1)).saveAll(Mockito.any());
    }

    @Test
    void Should_ThrowsException_When_NotValidArticlesOnStock() throws Exception {
        Mockito.doReturn(List.of()).when(articlePersistencePort).findAllById(Mockito.any());

        postSupplyWithAuxDepotToken()
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.ERROR_PROCESSING_OPERATION + Article.class.getSimpleName()))
            .andExpect(jsonPath(ConsUtils.FIELD_ID_PATH).value(ExceptionResponse.ID_NOT_FOUND));

        Mockito.verify(articlePersistencePort, Mockito.times(0)).saveAll(Mockito.any());
    }

    /*** Cart addition ***/
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_ThrowsException_When_ArticleNotFound() throws Exception {
        createArticle();

        itemsReqDTO.setItems(Set.of(ITEM_QUANTITY_DTO_2));
        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemsReqDTO))
                        .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.ERROR_PROCESSING_OPERATION + Article.class.getSimpleName()))
                .andExpect(jsonPath(ConsUtils.FIELD_ID_PATH).value(ExceptionResponse.ID_NOT_FOUND))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_ThrowsException_When_NotEnoughStockForCart() throws Exception {
        createArticle();

        itemsReqDTO.setItems(Set.of(ItemQuantityDTO.builder().articleId(ConsUtils.LONG_1).quantity(ConsUtils.LONG_10).build()));
        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemsReqDTO))
                        .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.STOCK_WILL_BE_AVAILABLE_SOON))
                .andExpect(jsonPath(ConsUtils.FIELD_ID_PATH).value(String.format(ExceptionResponse.NOT_ENOUGH_STOCK, ConsUtils.LONG_1)))
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_ThrowsException_When_NotValidConstraintArticleCategoryQuantity() throws Exception {
        createArticle();
        saveThreeArticlesOfSameCategory();

        itemsReqDTO.setItems(getFourArticlesOfSameCategory());
        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemsReqDTO))
                        .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.FAILED_CONSTRAINT_FOR_ARTICLE_CATEGORY))
                .andExpect(jsonPath(ConsUtils.FIELD_CATEGORY_PATH).value(ExceptionResponse.EXCEEDED))
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_Get200_When_ValidPayload() throws Exception {
        createArticle();

        mockMvc.perform(post(ConsUtils.builderPath().withCart().withArticles().build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemsReqDTO))
                        .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(status().isOk());
    }

    /*** Get all cart items ***/
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_GetAllCartItems_When_ValidParams() throws Exception {
        createArticle();

        mockMvc.perform(get(ConsUtils.builderPath().withCart().withArticles().withArticlesIds().build(), ConsUtils.LONG_1)
                .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.LONG_1))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_Return200_When_IdsNotFound() throws Exception {
        createArticle();

        mockMvc.perform(get(ConsUtils.builderPath().withCart().withArticles().withArticlesIds().build(), ConsUtils.LONG_10)
                .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.INTEGER_0))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_Return200_When_FilterByIdAndCategoryAndBrandNameASC() throws Exception {
        createArticle();

        mockMvc.perform(get(ConsUtils.builderPath().withCart().withArticles().withArticlesIds().build(), ConsUtils.LONG_1)
                        .params(buildParams(Map.of(ConsUtils.COLUMNS_PARAM, ConsUtils.CATEGORY_PARAM_VALUE + ConsUtils.COMMA_DELIMITER + ConsUtils.BRAND_PARAM_VALUE)))
                        .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.INTEGER_1))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_Return200_When_FilterByIdAndCategoryAndBrandNameDESC() throws Exception {
        createArticle();

        mockMvc.perform(get(ConsUtils.builderPath().withCart().withArticles().withArticlesIds().build(), ConsUtils.LONG_1)
                        .params(buildParams(Map.of(ConsUtils.COLUMNS_PARAM, ConsUtils.CATEGORY_PARAM_VALUE + ConsUtils.COMMA_DELIMITER + ConsUtils.BRAND_PARAM_VALUE)))
                        .params(buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.SORT_DESC_VALUE)))
                        .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getClientToken()))
                .andExpect(jsonPath(ConsUtils.FIELD_TOTAL_ELEMENTS).value(ConsUtils.INTEGER_1))
                .andExpect(status().isOk());
    }

    /*** Articles with price ***/
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void Should_Return200_When_GetAllArticlesWithPrice() throws Exception {
        createArticle();

        mockMvc.perform(get(ConsUtils.builderPath().withArticles().withArticlesIds().build(), ConsUtils.LONG_1))
                .andExpect(jsonPath(ConsUtils.BASE_MATCHER, Matchers.hasSize(ConsUtils.INTEGER_1)))
                .andExpect(status().isOk());
    }

    /*** Common ***/
    private Set<ItemQuantityDTO> getFourArticlesOfSameCategory() {
        Set<ItemQuantityDTO> items = new HashSet<>();

        for (int i = 1; i < 5; i++) {
            ItemQuantityDTO item = new ItemQuantityDTO();

            item.setArticleId((long) i);
            item.setQuantity(ConsUtils.LONG_1);

            items.add(item);
        }

        return items;
    }

    private void saveThreeArticlesOfSameCategory() {
        List<Article> articles = new ArrayList<>();
        Brand brand = new Brand(ConsUtils.TEST_NAME, ConsUtils.VALID_DESC);
        brand.setId(ConsUtils.LONG_1);

        Category category = new Category(ConsUtils.TEST_NAME, ConsUtils.VALID_DESC);
        category.setId(ConsUtils.LONG_1);

        for (int i = 2; i < 5; i++) {
            Article article = new Article();

            article.setId((long) i);
            article.setBrand(brand);
            article.setCategories(Set.of(category));
            article.setQuantity(ConsUtils.LONG_10);
            article.setName(ConsUtils.TEST_NAME + i);
            article.setPrice(BigDecimal.TEN);
            article.setDescription(ConsUtils.VALID_DESC);

            articles.add(article);
        }

        articles.forEach(articlePersistencePort::save);
    }

    private void createArticle() throws Exception {
        sentPostToCreateEntity(mapper.writeValueAsString(categoryReqDTO), ConsUtils.builderPath().withCategories().build()).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(brandReqDTO), ConsUtils.builderPath().withBrands().build()).andExpect(status().isCreated());
        sentPostToCreateEntity(mapper.writeValueAsString(articleReqDTO), ConsUtils.builderPath().withArticles().build()).andExpect(status().isCreated());
    }

    private ResultActions postSupplyWithAuxDepotToken() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(ConsUtils.SUPPLY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(itemsReqDTO))
                .header(ConsUtils.AUTHORIZATION, ConsUtils.BEARER + getAuxDepotToken()));
    }

    @Test
    void Should_ThrowsAnException_When_InvalidSortDirection() throws Exception {
        getAllPageableEntities(ConsUtils.builderPath().withBrands().build(), buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.NON_EXISTING_DIRECTION_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.NOT_VALID_PARAM))
                .andExpect(jsonPath(ConsUtils.FIELD_ERROR, Matchers.aMapWithSize(1)))
                .andExpect(jsonPath(ConsUtils.FIELD_DIRECTION_PATH).value(ConsUtils.NON_EXISTING_DIRECTION_COLUMN));

        getAllPageableEntities(ConsUtils.builderPath().withCategories().build(), buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.NON_EXISTING_DIRECTION_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.NOT_VALID_PARAM))
                .andExpect(jsonPath(ConsUtils.FIELD_ERROR, Matchers.aMapWithSize(1)))
                .andExpect(jsonPath(ConsUtils.FIELD_DIRECTION_PATH).value(ConsUtils.NON_EXISTING_DIRECTION_COLUMN));

        getAllPageableEntities(ConsUtils.builderPath().withArticles().build(), buildParams(Map.of(ConsUtils.DIRECTION_PARAM, ConsUtils.NON_EXISTING_DIRECTION_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ConsUtils.FIELD_MESSAGE).value(ExceptionResponse.NOT_VALID_PARAM))
                .andExpect(jsonPath(ConsUtils.FIELD_ERROR, Matchers.aMapWithSize(1)))
                .andExpect(jsonPath(ConsUtils.FIELD_DIRECTION_PATH).value(ConsUtils.NON_EXISTING_DIRECTION_COLUMN));
    }

    private void assertValidPageDTOFormResponseFromResult(ResultActions res, Integer elements) throws Exception {
        final int ELEMENTS_FOR_PAGE = 9;
        res.andExpect(jsonPath(ConsUtils.BASE_MATCHER, Matchers.aMapWithSize(ELEMENTS_FOR_PAGE)))
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
        CustomUserDetails userDetail = new CustomUserDetails(ConsUtils.TEST_NAME, ConsUtils.PASSWORD, Set.of(new SimpleGrantedAuthority(ConsUtils.ROLE.concat(ConsUtils.AUX_DEPOT))), ConsUtils.LONG_1);
        return JwtUtils.createToken(new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));
    }

    private String getClientToken() {
        CustomUserDetails userDetail = new CustomUserDetails(ConsUtils.TEST_NAME, ConsUtils.PASSWORD, Set.of(new SimpleGrantedAuthority(ConsUtils.ROLE.concat(ConsUtils.CLIENT))), ConsUtils.LONG_1);
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
