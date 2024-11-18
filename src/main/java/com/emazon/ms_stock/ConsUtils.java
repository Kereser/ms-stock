package com.emazon.ms_stock;

import java.util.List;
import java.util.Set;

public class ConsUtils {

  private ConsUtils() {
  }

  public static PathBuilder builderPath() {
    return new PathBuilder();
  }

  public static final Long PLUS_30_MINUTES = 1800000L;
  public static final String JWT_KEY = "${security.jwt.key.private}";
  public static final String JWT_USER = "${security.jwt.user.generator}";
  public static final String COMMA_DELIMITER = ",";
  public static final String COLON_DELIMITER = ":";
  public static final String SPACE = " ";

  public static final String EMPTY = "";

  public static final String ASC = "ASC";
  public static final String NAME = "name";
  public static final String INTEGER_STR_0 = "0";
  public static final String INTEGER_STR_20 = "20";

  public static final String BASIC_CATEGORIES_URL = "/stock/categories";
  public static final String BASIC_BRAND_URL = "/stock/brands";
  public static final String BASIC_ARTICLES_URL = "/stock/articles";
  public static final String SUPPLY_TEST_URL = "/stock/articles/supply";
  public static final String SUPPLY_URL = "/articles/supply";
  public static final String ARTICLES_URL = "/articles";

  public static final String CLIENT = "CLIENT";
  public static final String ADMIN = "ADMIN";
  public static final String AUX_DEPOT = "AUX_DEPOT";
  public static final String ROLE = "ROLE_";

  public static final String AUTHORIZATION = "Authorization";
  public static final String BEARER = "Bearer ";
  public static final String ARTICLE = "article";

  public static final String LAST_TEST_NAME = "Zipikaka";
  public static final String BASE_MATCHER = "$";
  public static final String NAME_OF_FIRST_CATEGORY_ON_ARTICLE = "$.content[0].categories[0].name";

  public static final String TEST = "test";
  public static final String TEST_NAME = "Test name";
  public static final String PASSWORD = "password";
  public static final String VALID_DESC = "description";
  public static final String FIELD_ERROR = "$.fieldErrors";
  public static final String FIELD_MESSAGE = "$.message";
  public static final String FIELD_TOTAL_ELEMENTS = "$.totalElements";
  public static final String FIELD_TOTAL_PAGES = "$.totalPages";
  public static final String FIELD_PAGEABLE_PAGE_SIZE = "$.pageable.pageSize";
  public static final String FIELD_CURRENT_PAGE = "$.currentPage";
  public static final String FIELD_NAME_PATH = "$.fieldErrors.name";
  public static final String FIELD_CONTENT = "$.content";
  public static final String FIELD_ID_PATH = "$.fieldErrors.id";
  public static final String FIELD_DESCRIPTION_PATH = "$.fieldErrors.description";
  public static final String FIELD_CATEGORY_IDS_PATH = "$.fieldErrors.categoryIds";
  public static final String FIELD_CATEGORY_PATH = "$.fieldErrors.categories";
  public static final String FIELD_PRICE_PATH = "$.fieldErrors.price";
  public static final String FIELD_QUANTITY_PATH = "$.fieldErrors.quantity";
  public static final String FIELD_QUANTITY_PATH_ARRAY = "$.fieldErrors['items[].quantity']";
  public static final String FIELD_ARTICLE_ID_PATH_ARRAY = "$.fieldErrors['items[].articleId']";
  public static final String FIELD_BRAND_ID_PATH = "$.fieldErrors.brandId";
  public static final String FIELD_DIRECTION_PATH = "$.fieldErrors.direction";
  public static final String FIELD_ITEMS = "$.fieldErrors.items";


  public static final String REQUIRED_BODY = "Required request body is missing";

  public static final Integer FIELD_WITH_ERRORS_AT_CATEGORY = 2;
  public static final Integer FIELD_WITH_ERRORS_AT_BRAND = 2;
  public static final Integer FIELD_WITH_ERRORS_AT_ARTICLE = 6;

  public static final Long LONG_0 = 0L;
  public static final Long LONG_1 = 1L;
  public static final Long LONG_2 = 2L;
  public static final Long LONG_10 = 10L;

  public static final Integer INTEGER_0 = 0;
  public static final Integer INTEGER_1 = 1;
  public static final Integer INTEGER_2 = 2;
  public static final Integer INTEGER_3 = 3;
  public static final Integer INTEGER_4 = 4;
  public static final Integer INTEGER_20 = 20;

  public static final String PLUS_FIFTY_CHARACTERS = "d".repeat(51);
  public static final String PLUS_NINETY_CHARACTERS = "d".repeat(91);
  public static final String PLUS_TWO_HUNDRED_CHARACTERS = "d".repeat(201);

  public static final String INVALID_SORT_CRITERIA = "Invalid sort criteria in request param";
  public static final String NON_EXISTING_SORTING_COLUMN = "Non:existing:column";
  public static final String COLUMN_PARAM = "column";
  public static final String COLUMNS_PARAM = "columns";
  public static final String SORT_ASC_VALUE = "ASC";
  public static final String SORT_DESC_VALUE = "DESC";

  public static final String NON_EXISTING_DIRECTION_COLUMN = "Non:existing:DIRECTION";
  public static final String DIRECTION_PARAM = "direction";

  public static final String CATEGORIES_PARAM_VALUE = "categories:name";
  public static final String CATEGORY_NAME = "categoryName";
  public static final String CATEGORY = "category";
  public static final String ANYTHING = "anything";
  public static final String CATEGORIES = "categories";
  public static final String BRAND_PARAM_VALUE = "brand:name";
  public static final String BRAND_NAME = "brandName";
  public static final String BRAND = "brand";
  public static final String NAME_PARAM_VALUE = "name";
  public static final String DESCRIPTION_PARAM_VALUE = "description";

  /*** Routes ***/
  public static final String WITH_ROLLBACK = "/rollback";
  public static final String WITH_ALL = "/all";
  public static final String NAMES_PARAM = "names";

  public static final String ID = "id";

  public static final String ARTICLE_IDS = "articleIds";
  public static final String VALIDATE_CART_URL = "/cart/articles";
  public static final String PROCESS_CART_PURCHASE_URL = "/cart/articles/purchase";
  public static final String PROCESS_CART_ROLLBACK_URL = "/cart/articles/rollback";
  public static final String GET_ALL_ARTICLES = "/articles/all";
  public static final String ARTICLES_FOR_CART_URL = "/cart/articles/{articleIds}";
  public static final String ARTICLE_PRICE_URL = "/articles/{articleIds}";

  /*** Security ***/
  public static final String POST = "POST";
  public static final String GET = "GET";
  public static final String PUT = "PUT";
  public static final String DELETE = "DELETE";
  public static final String FRONT_URL = "http://localhost:4200";
  public static final String BASE_URL = "http://localhost:8080";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String REQUESTED_WITH = "X-Requested-With";
  public static final String MATCH_ALL = "/**";

  public static final List<String> PARAMS_FOR_ARTICLES_ON_CART = List.of(CATEGORIES_PARAM_VALUE,
    NAME_PARAM_VALUE,
    DESCRIPTION_PARAM_VALUE);
  public static final Set<String> COMBINED_PARAMS_FOR_ARTICLE = Set.of(CATEGORIES_PARAM_VALUE, BRAND_PARAM_VALUE);

  public static final String BASIC_URL = "/stock";
  public static final String CATEGORIES_URL = "/categories";
  public static final String BRAND_URL = "/brands";
  public static final String BRANDS_BY_NAME_URL = "/brands/by-names";
  public static final String CATEGORIES_BY_NAMES_URL = "/categories/by-names";

  public static final String SWAGGER_URL = "/swagger-ui/**";
  public static final String SWAGGER_DOCS_URL = "/v3/api-docs/**";

  public static class PathBuilder {
    private String finalPath = BASIC_URL;

    public PathBuilder withSupply() {
      this.finalPath += "/supply";
      return this;
    }

    public PathBuilder withArticles() {
      this.finalPath += ConsUtils.ARTICLES_URL;
      return this;
    }

    public PathBuilder withArticlesIds() {
      this.finalPath += "/{articleIds}";
      return this;
    }

    public PathBuilder withCategories() {
      this.finalPath += CATEGORIES_URL;
      return this;
    }

    public PathBuilder withCategoriesByName() {
      this.finalPath += CATEGORIES_BY_NAMES_URL;
      return this;
    }

    public PathBuilder withBrands() {
      this.finalPath += BRAND_URL;
      return this;
    }

    public PathBuilder withBrandsByName() {
      this.finalPath += BRANDS_BY_NAME_URL;
      return this;
    }

    public PathBuilder withCart() {
      this.finalPath += "/cart";
      return this;
    }

    public PathBuilder withPurchase() {
      this.finalPath += "/purchase";
      return this;
    }

    public PathBuilder withRollback() {
      this.finalPath += WITH_ROLLBACK;
      return this;
    }

    public PathBuilder withAll() {
      this.finalPath += WITH_ALL;
      return this;
    }

    public PathBuilder withAnything() {
      this.finalPath += "/**";
      return this;
    }

    public String build() {
      return finalPath;
    }
  }
}
