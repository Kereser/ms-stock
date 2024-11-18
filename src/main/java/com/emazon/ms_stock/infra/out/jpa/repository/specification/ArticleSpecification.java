package com.emazon.ms_stock.infra.out.jpa.repository.specification;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.infra.out.jpa.entity.ArticleEntity;
import jakarta.persistence.criteria.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleSpecification {
  public static Specification<ArticleEntity> hasBrandName(String brandName) {
    return (root, query, criteriaBuilder) -> {
      if (brandName == null || brandName.isBlank()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get(ConsUtils.BRAND).get(ConsUtils.NAME), brandName);
    };
  }

  public static Specification<ArticleEntity> hasCategoryName(String categoryName) {
    return (root, query, criteriaBuilder) -> {
      if (categoryName == null || categoryName.isBlank()) {
        return criteriaBuilder.conjunction();
      }
      return criteriaBuilder.equal(root.get(ConsUtils.CATEGORIES).get(ConsUtils.NAME), categoryName);
    };
  }

  public static Specification<ArticleEntity> hasArticleIds(Set<Long> articleIds) {
    return (root, query, criteriaBuilder) -> articleIds == null || articleIds.isEmpty() ?
                                               criteriaBuilder.conjunction() :
                                               root.get(ConsUtils.ID).in(articleIds);
  }

  public static Specification<ArticleEntity> orderBy(String field, boolean ascending) {
    return (root, query, criteriaBuilder) -> {
      if (field == null) {
        return criteriaBuilder.conjunction();
      }

      String[] fieldParts = field.split(ConsUtils.COLON_DELIMITER);
      Path<?> path = root;

      for (String part : fieldParts) {
        path = path.get(part);
      }

      if (query == null) {
        return criteriaBuilder.conjunction();
      }

      if (ascending) {
        query.orderBy(criteriaBuilder.asc(path));
      } else {
        query.orderBy(criteriaBuilder.desc(path));
      }
      return criteriaBuilder.conjunction();
    };
  }
}
