package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.application.dto.PageHandler;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.infra.exception.BrandAlreadyExists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrandUseCaseTest {

    @Mock
    private IBrandPersistencePort persistencePort;

    @InjectMocks
    private BrandUseCase servicePort;

    private final Brand brand = new Brand("test name", "desc");

    @Test
    void Should_SaveBrand_When_UniqueName() {
        Mockito.when(persistencePort.findByName(Mockito.anyString())).thenReturn(Optional.empty());

        servicePort.save(brand);
        Mockito.verify(persistencePort, Mockito.times(1)).findByName(Mockito.anyString());
    }

    @Test
    void Should_throwsAnException_When_BrandAlreadyExists() {
        Mockito.when(persistencePort.findByName(Mockito.anyString())).thenReturn(Optional.of(brand));

        assertThrows(BrandAlreadyExists.class, () -> servicePort.save(brand));
    }

    @Test
    void Should_InteractWithPersistencePortOneTimeOnly_When_ValidRequest() {
        servicePort.findAllPageable(PageHandler.builder().page(0).pageSize(20).build());

        Mockito.verify(persistencePort, Mockito.times(1)).findAllPageable(Mockito.any());
    }
}