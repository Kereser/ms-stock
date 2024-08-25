package com.emazon.ms_stock.infra.config;

import com.emazon.ms_stock.domain.api.IBrandServicePort;
import com.emazon.ms_stock.domain.api.ICategoryServicePort;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.domain.spi.ICategoryPersistencePort;
import com.emazon.ms_stock.domain.use_cases.BrandUseCase;
import com.emazon.ms_stock.domain.use_cases.CategoryUseCase;
import com.emazon.ms_stock.infra.out.jpa.adapter.BrandJpaAdapter;
import com.emazon.ms_stock.infra.out.jpa.adapter.CategoryJpaAdapter;
import com.emazon.ms_stock.infra.out.jpa.mapper.BrandEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.mapper.CategoryEntityMapper;
import com.emazon.ms_stock.infra.out.jpa.repository.BrandJpaRepository;
import com.emazon.ms_stock.infra.out.jpa.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;
    private final BrandJpaRepository brandJpaRepository;
    private final BrandEntityMapper brandEntityMapper;

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryJpaRepository, categoryEntityMapper);
    }

    @Bean
    public ICategoryServicePort categoryServicePort() {
        return new CategoryUseCase(categoryPersistencePort());
    }

    @Bean
    public IBrandPersistencePort brandPersistencePort() {
        return new BrandJpaAdapter(brandJpaRepository, brandEntityMapper);
    }

    @Bean
    public IBrandServicePort brandServicePort() {
        return new BrandUseCase(brandPersistencePort());
    }
}
