package com.emazon.ms_stock.infra.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "article")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long quantity;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "article_category",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"article_id", "category_id"})
    )
    @Column(nullable = false)
    @Builder.Default
    private Set<CategoryEntity> categories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @CreatedDate
    @Builder.Default
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void addCategories(Set<CategoryEntity> cg) {
        categories.addAll(cg);
        cg.forEach(c -> c.addArticles(Set.of(this)));
    }

    public void removeCategories(Set<CategoryEntity> cg) {
        categories.removeAll(cg);
        cg.forEach(c -> c.removeArticles(Set.of(this)));
    }
}
