package com.emazon.ms_stock.infra.exception;

import com.emazon.ms_stock.domain.model.Article;

public class ArticleAlreadyExists extends BaseEntityException {
    private static final String UNIQUE_FIELD = "Name";
    public ArticleAlreadyExists() {
        super(Article.class.getSimpleName(), UNIQUE_FIELD);
    }
}
