package com.emazon.ms_stock.infra.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long quantity;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "article_category",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"article_id", "category_id"})
    )
    @Builder.Default
    private Set<CategoryEntity> categories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    public void addCategories(Set<CategoryEntity> cg) {
        categories.addAll(cg);
        cg.forEach(c -> c.addArticles(Set.of(this)));
    }

    public void removeCategories(Set<CategoryEntity> cg) {
        categories.removeAll(cg);
        cg.forEach(c -> c.removeArticles(Set.of(this)));
    }
}
