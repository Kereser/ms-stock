package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.application.dto.ItemQuantityDTO;
import com.emazon.ms_stock.application.dto.ItemsReqDTO;
import com.emazon.ms_stock.application.dto.out.ArticlesPriceDTO;
import com.emazon.ms_stock.domain.model.Article;

import java.util.List;
import java.util.Set;

public interface IArticleServicePort extends IBasicCrudServicePort<Article> {
    void addSupply(Set<ItemQuantityDTO> dto);
    void validationsOnStockForCart(Set<ItemQuantityDTO> dto);
    Set<ArticlesPriceDTO> getArticlesPrice(Set<Long> articleIds);
    List<Article> getAllArticles(List<Long> articleIds);

    void processStockReduction(ItemsReqDTO itemsReqDTO);

    void processRollback(ItemsReqDTO itemsReqDTO);
}
