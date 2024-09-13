package com.emazon.ms_stock.domain.api;

import com.emazon.ms_stock.application.dto.supply.SupplyReqDTO;
import com.emazon.ms_stock.domain.model.Article;

import java.util.Set;

public interface IArticleServicePort extends IBasicCrudServicePort<Article> {
    void addSupply(Set<SupplyReqDTO> dto);
}
