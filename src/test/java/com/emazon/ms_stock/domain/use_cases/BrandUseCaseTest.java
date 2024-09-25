package com.emazon.ms_stock.domain.use_cases;

import com.emazon.ms_stock.ConsUtils;
import com.emazon.ms_stock.application.dto.handlers.PageHandler;
import com.emazon.ms_stock.domain.model.Brand;
import com.emazon.ms_stock.domain.spi.IBrandPersistencePort;
import com.emazon.ms_stock.infra.exception.BrandAlreadyExists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrandUseCaseTest {

    @Mock
    private IBrandPersistencePort persistencePort;

    @InjectMocks
    private BrandUseCase servicePort;

    private final Brand brand = new Brand(ConsUtils.TEST_NAME, ConsUtils.VALID_DESC);

    @Test
    void Should_SaveBrand_When_UniqueName() {
        Mockito.when(persistencePort.findByName(Mockito.anyString())).thenReturn(Optional.empty());

        servicePort.save(brand);
        Mockito.verify(persistencePort, Mockito.times(ConsUtils.INTEGER_1)).findByName(Mockito.anyString());
    }

    @Test
    void Should_throwsAnException_When_BrandAlreadyExists() {
        Mockito.when(persistencePort.findByName(Mockito.anyString())).thenReturn(Optional.of(brand));

        assertThrows(BrandAlreadyExists.class, () -> servicePort.save(brand));
    }

    @Test
    void Should_InteractWithPersistencePortOneTimeOnly_When_ValidRequest() {
        servicePort.findAllPageable(PageHandler.builder()
                        .page(ConsUtils.INTEGER_0)
                        .pageSize(ConsUtils.INTEGER_20)
                        .direction(Sort.Direction.ASC.name())
                        .column(ConsUtils.NAME_PARAM_VALUE)
                .build());

        Mockito.verify(persistencePort, Mockito.times(ConsUtils.INTEGER_1)).findAllPageable(Mockito.any());
    }
}