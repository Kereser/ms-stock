package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.domain.model.Article;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.model.Category;
import com.emazon.ms_stock.domain.spi.IArticlePersistencePort;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.infra.exception.NoDataFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

        Mockito.verify(brandPersistencePort, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(1)).save(ar);
        Assertions.assertEquals(1, ar.getCategories().size());
    }

    @Test
    void Should_InteractWithCorrectMethods_DependingOn_ColumnAndDirectionOnPayload() {
        PageHandler page = getBasicPageAsc(Sort.Direction.ASC);
        page.setColumn("name");
        articleUseCase.findAllPageable(page);

        Mockito.verify(articlePersistencePort, Mockito.times(1)).findAllPageable(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(0)).findAllByCategoryNameAsc(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(0)).findAllByCategoryNameDesc(Mockito.any());

        Mockito.reset(articlePersistencePort);
        page.setColumn(Article.INNER_SORT_CATEGORY_NAME);
        articleUseCase.findAllPageable(page);

        Mockito.verify(articlePersistencePort, Mockito.times(1)).findAllByCategoryNameAsc(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(0)).findAllPageable(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(0)).findAllByCategoryNameDesc(Mockito.any());

        Mockito.reset(articlePersistencePort);
        page = getBasicPageAsc(Sort.Direction.DESC);
        page.setColumn(Article.INNER_SORT_CATEGORY_NAME);
        articleUseCase.findAllPageable(page);

        Mockito.verify(articlePersistencePort, Mockito.times(1)).findAllByCategoryNameDesc(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(0)).findAllPageable(Mockito.any());
        Mockito.verify(articlePersistencePort, Mockito.times(0)).findAllByCategoryNameAsc(Mockito.any());
    }

    private Article createBasicArticle() {
        Article article = new Article();
        article.setName(ARTICLE_NAME);
        article.setDescription(TEST_DESCRIPTION);
        article.setPrice(BigDecimal.TEN);
        article.setQuantity(2L);

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
                .column("")
                .page(0)
                .pageSize(20)
                .direction(dir.toString())
                .build();
    }
}