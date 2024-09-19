package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.application.dto.ItemQuantityDTO;
import com.emazon.ms_stock.domain.model.Article;

import java.util.Set;

public interface IArticleServicePort extends IBasicCrudServicePort<Article> {
    void addSupply(Set<ItemQuantityDTO> dto);
    void handleCartAdditionValidations(Set<ItemQuantityDTO> dto);
}
