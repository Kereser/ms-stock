package com.emazon.ms_stock;

public class ConsUtils {
    public static final String BASIC_CATEGORIES_URL = "/stock/categories";
    public static final String BASIC_BRAND_URL = "/stock/brands";
    public static final String BASIC_ARTICLES_URL = "/stock/articles";

    public static final String TEST_NAME = "Test name";
    public static final String DESC_NAME = "description";
    public static final String FIELD_ERROR = "$.fieldErrors";
    public static final String FIELD_MESSAGE = "$.message";
    public static final String FIELD_NAME_PATH = "$.fieldErrors.name";
    public static final String FIELD_DESCRIPTION_PATH = "$.fieldErrors.description";
    public static final String FIELD_CATEGORYIDS_PATH = "$.fieldErrors.categoryIds";
    public static final String FIELD_PRICE_PATH = "$.fieldErrors.price";
    public static final String FIELD_QUANTITY_PATH = "$.fieldErrors.quantity";
    public static final String FIELD_BRANDID_PATH = "$.fieldErrors.brandId";
    public static final String FIELD_DIRECTION_PATH = "$.fieldErrors.direction";

    public static final Integer FIELD_WITH_ERRORS_AT_CATEGORY = 2;
    public static final Integer FIELD_WITH_ERRORS_AT_BRAND = 2;
    public static final Integer FIELD_WITH_ERRORS_AT_ARTICLE = 6;

    public static final Long LONG_ONE = 1L;

    public static final String PLUS_FIFTY_CHARACTERS = "d".repeat(51);
    public static final String PLUS_NINETY_CHARACTERS = "d".repeat(91);
    public static final String PLUS_TWO_HUNDRED_CHARACTERS = "d".repeat(201);

    public static final String INVALID_SORT_CRITERIA = "Invalid sort criteria in request param";
    public static final String NON_EXISTING_SORTING_COLUMN = "Non:existing:column";
    public static final String COLUMN_PARAM = "column";

    public static final String NON_EXISTING_DIRECTION_COLUMN = "Non:existing:DIRECTION";
    public static final String DIRECTION_PARAM = "direction";
}
