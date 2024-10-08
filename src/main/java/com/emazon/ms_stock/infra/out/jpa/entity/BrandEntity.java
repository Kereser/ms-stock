package com.emazon.ms_stock.infra.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "brand")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "brand")
    private List<ArticleEntity> article;
}
