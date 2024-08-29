package com.emazon.ms_stock.infra.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "categories")
    private Set<ArticleEntity> articles;

    public void addArticles(Set<ArticleEntity> ar) {
        articles.addAll(ar);
    }

    public void removeArticles(Set<ArticleEntity> ar) {
        articles.removeAll(ar);
    }
}
