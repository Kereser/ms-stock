package com.emazon.ms_stock.infra.out.jpa.repository;

import com.emazon.ms_stock.infra.out.jpa.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandJpaRepository extends JpaRepository<BrandEntity, Long> {
    Optional<BrandEntity> findByName(String name);

    List<BrandEntity> findByNameIn(List<String> names);
}
