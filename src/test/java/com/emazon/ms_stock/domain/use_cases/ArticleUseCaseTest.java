package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.TestCreationUtils;
import com.emazon.ms_stock.application.dto.ItemQuantityDTO;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.ArticleCategoryQuantityException;
import com.emazon.ms_stock.infra.exception.NoDataFoundException;
import com.emazon.ms_stock.infra.exception.NotSufficientStock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArticleUseCaseTest {

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IBrandPersistencePort brandPersistencePort;

    @Mock
    private IArticlePersistencePort articlePersistencePort;

    @InjectMocks
    private ArticleUseCase articleUseCase;

    private static Long nameSuffix = 0L;
    private static final String ARTICLE_NAME = "article" + nameSuffix++;
    private static Long descSuffix = 0L;
    private static final String TEST_DESCRIPTION = "anything" + descSuffix++;
    private static Long testSuffix = 0L;
    private static final String TEST_NAME = "test" + testSuffix++;
    private final Category category = new Category(TEST_NAME, TEST_DESCRIPTION);
    private final Brand brand = new Brand(TEST_NAME, TEST_DESCRIPTION);

    @Test
    void Should_ThrowsExceptions_WhenNotValidCategoryOrBrandIds() {
        Mockito.when(categoryPersistencePort.findAllById(Mockito.any())).thenReturn(new ArrayList<>());

        Article arCategory = createArticleWithCategoryAndBrand();
        assertThrows(NoDataFoundException.class, () -> articleUseCase.save(arCategory));

        Mockito.when(categoryPersistencePort.findAllById(Mockito.any())).thenReturn(List.of(new Category("Cate", "anything")));
        Mockito.when(brandPersistencePort.findById(Mockito.any())).thenReturn(Optional.empty());

        Article arBrand = createArticleWithCategoryAndBrand();
        assertThrows(NoDataFoundException.class, () -> articleUseCase.save(arBrand));
    }

    @Test
    void Should_SaveArticle_When_ValidPayload() {
        Mockito.when(categoryPersistencePort.findAllById(Mockito.any())).thenReturn(List.of(new Category(TEST_NAME, TEST_DESCRIPTION)));
        Mockito.when(brandPersistencePort.findById(Mockito.any())).thenReturn(Optional.of(new Brand(TEST_NAME, TEST_DESCRIPTION)));

        Article ar = createArticleWithCategoryAndBrand();
        articleUseCase.save(ar);

        Mockito.verify(brandPersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findById(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).save(ar);
        Assertions.assertEquals(1, ar.getCategories().size());
    }

    @Test
    void Should_InteractWithCorrectMethods_DependingOn_ColumnAndDirectionOnPayload() {
        PageHandler page = getBasicPageAsc(Sort.Direction.ASC);
        articleUseCase.findAllPageable(page);

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllByArticleIdPageable(Mockito.any(), Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).findAllByCategoryNameAsc(Mockito.any(), Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).findAllByCategoryNameDesc(Mockito.any(), Mockito.any());

        Mockito.reset(articlePersistencePort);
        page.setColumn(Article.INNER_SORT_CATEGORY_NAME);
        articleUseCase.findAllPageable(page);

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllByCategoryNameAsc(Mockito.any(), Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).findAllByArticleIdPageable(Mockito.any(), Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).findAllByCategoryNameDesc(Mockito.any(), Mockito.any());

        Mockito.reset(articlePersistencePort);
        page = getBasicPageAsc(Sort.Direction.DESC);
        page.setColumn(Article.INNER_SORT_CATEGORY_NAME);
        articleUseCase.findAllPageable(page);

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllByCategoryNameDesc(Mockito.any(), Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).findAllByArticleIdPageable(Mockito.any(), Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).findAllByCategoryNameAsc(Mockito.any(), Mockito.any());
    }

    @Test
    void Should_ThrowsException_When_NotArticleFound() {
        Set<ItemQuantityDTO> itemsDTO = Set.of(TestCreationUtils.createItemQuantity());
        Assertions.assertThrows(NoDataFoundException.class, () -> articleUseCase.addSupply(itemsDTO));

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).save(Mockito.any());
    }

    @Test
    void Should_PassValidations_When_ValidPayload() {
        Set<ItemQuantityDTO> itemsDTO = Set.of(TestCreationUtils.createItemQuantity());

        Mockito.doReturn(List.of(TestCreationUtils.createArticle())).when(articlePersistencePort).findAllById(Mockito.any());
        articleUseCase.addSupply(itemsDTO);

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_0)).save(Mockito.any());
    }

    @Test
    void Should_ThrowsException_When_IdNotFound() {
        Mockito.doReturn(createArticles(ConsUtils.INTEGER_1)).when(articlePersistencePort).findAllById(Mockito.any());

        Set<ItemQuantityDTO> itemsSet = getItemsQuantityDTO(ConsUtils.INTEGER_2);
        Assertions.assertThrows(NoDataFoundException.class, () -> articleUseCase.validationsOnStockForCart(itemsSet));
    }

    @Test
    void Should_ThrowsException_When_NotEnoughStock() {
        Mockito.doReturn(createArticles(ConsUtils.INTEGER_1)).when(articlePersistencePort).findAllById(Mockito.any());

        Set<ItemQuantityDTO> itemsSet = getItemsQuantityDTO(ConsUtils.INTEGER_1);
        itemsSet.stream().findFirst().orElse(ItemQuantityDTO.builder().build()).setQuantity(ConsUtils.LONG_10);
        Assertions.assertThrows(NotSufficientStock.class, () -> articleUseCase.validationsOnStockForCart(itemsSet));
    }

    @Test
    void Should_ThrowsException_When_NotMeetArticleCategoryConstraints() {
        Mockito.doReturn(createArticles(ConsUtils.INTEGER_4)).when(articlePersistencePort).findAllById(Mockito.any());

        Set<ItemQuantityDTO> itemsSet = getItemsQuantityDTO(ConsUtils.INTEGER_4);
        Assertions.assertThrows(ArticleCategoryQuantityException.class, () -> articleUseCase.validationsOnStockForCart(itemsSet));
    }

    @Test
    void Should_HaveValidInteractions_When_ValidPayloadOnValidationsOnStock() {
        Mockito.doReturn(List.of(TestCreationUtils.createArticle())).when(articlePersistencePort).findAllById(Mockito.any());
        articleUseCase.validationsOnStockForCart(Set.of(TestCreationUtils.createItemQuantity()));

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllById(Mockito.any());
    }

    @Test
    void Should_HaveValidInteractions_When_ValidPayload() {
        PageHandler page = getBasicPageAsc(Sort.Direction.ASC);
        page.setColumn(ConsUtils.CATEGORY_PARAM_VALUE + "," + ConsUtils.BRAND_PARAM_VALUE);
        articleUseCase.findAllPageable(page);

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllByArticleIdsAndCategoryAndBrandNameAsc(Mockito.any(), Mockito.any());
    }

    /*** Buy cart flow ***/
    @Test
    void Should_InteractToGetArticles_When_ValidPayloadOnReduction() {
        Mockito.doReturn(List.of(TestCreationUtils.createArticle())).when(articlePersistencePort).findAllById(Mockito.any());
        Mockito.doReturn(List.of(TestCreationUtils.createCategory())).when(categoryPersistencePort).findAllById(Mockito.any());
        Mockito.doReturn(Optional.of(TestCreationUtils.createBrand())).when(brandPersistencePort).findById(Mockito.any());
        articleUseCase.processStockReduction(TestCreationUtils.createItemsReqDTO());

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllById(Mockito.any());
    }

    @Test
    void Should_InteractToGetArticles_When_ValidPayloadOnRollback() {
        Mockito.doReturn(List.of(TestCreationUtils.createArticle())).when(articlePersistencePort).findAllById(Mockito.any());
        Mockito.doReturn(List.of(TestCreationUtils.createCategory())).when(categoryPersistencePort).findAllById(Mockito.any());
        Mockito.doReturn(Optional.of(TestCreationUtils.createBrand())).when(brandPersistencePort).findById(Mockito.any());
        articleUseCase.processRollback(TestCreationUtils.createItemsReqDTO());

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllById(Mockito.any());
    }

    @Test
    void Should_GetAllArticles_When_ValidArticleIds() {
        articleUseCase.getAllArticles(List.of(ConsUtils.LONG_1));

        Mockito.verify(articlePersistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllById(Mockito.any());
    }

    private Set<ItemQuantityDTO> getItemsQuantityDTO(Integer quantity) {
        Set<ItemQuantityDTO> items = new HashSet<>();

        for (int i = 1; i < quantity + 1; i++) {
            ItemQuantityDTO item = new ItemQuantityDTO();

            item.setArticleId((long) i);
            item.setQuantity(ConsUtils.LONG_1);

            items.add(item);
        }

        return items;
    }

    private List<Article> createArticles(Integer quantity) {
        List<Article> list = new ArrayList<>();

        Category cat = new Category(ConsUtils.TEST_NAME, ConsUtils.VALID_DESC);
        cat.setId(ConsUtils.LONG_1);

        for (int i = 1; i < quantity + 1; i++) {
            Article article = new Article();
            article.setId((long) i);
            article.setName(ARTICLE_NAME);
            article.setDescription(TEST_DESCRIPTION);
            article.setPrice(BigDecimal.TEN);
            article.setQuantity(ConsUtils.LONG_2);
            article.setCategories(Set.of(cat));

            list.add(article);
        }

        return list;
    }

    private Article createBasicArticle() {
        Article article = new Article();
        article.setName(ARTICLE_NAME);
        article.setDescription(TEST_DESCRIPTION);
        article.setPrice(BigDecimal.TEN);
        article.setQuantity(ConsUtils.LONG_2);

        return article;
    }

    private Article createArticleWithCategoryAndBrand() {
        Article article = createBasicArticle();
        article.setCategories(Set.of(category));
        article.setBrand(brand);
        return article;
    }

    private PageHandler getBasicPageAsc(Sort.Direction dir) {
        return PageHandler.builder()
                .column(ConsUtils.NAME_PARAM_VALUE)
                .page(ConsUtils.INTEGER_0)
                .pageSize(ConsUtils.INTEGER_20)
                .direction(dir.toString())
                .filters(Map.of())
                .build();
    }
}