package io.github.agomezlucena.priceservice.application;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.PriceNotFoundException;
import io.github.agomezlucena.priceservice.domain.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPriceInfoUseCaseTest {
    @Mock
    private PriceRepository priceRepository;
    @InjectMocks
    private FindPriceInfoUseCase findPriceInfoUseCase;

    @Test
    void shouldThrowAPriceNotFoundExceptionWhenPriceNotFound() {
        var givenPriceInfoApplicationDateQuery = new PriceInfoApplicationDateQuery(80, 80, LocalDateTime.now());

        when(priceRepository.findPriceInfoByApplicationDate(
                        givenPriceInfoApplicationDateQuery.brandId(),
                        givenPriceInfoApplicationDateQuery.productId(),
                        givenPriceInfoApplicationDateQuery.applicationDate()
                )
        ).thenReturn(Optional.empty());

        assertThrows(
                PriceNotFoundException.class,
                () -> findPriceInfoUseCase.findPriceInfoByApplicationDate(givenPriceInfoApplicationDateQuery),
                "Price should not be found and a PriceNotFoundException should have been thrown"
        );
    }

    @Test
    void shouldReturnTheExpectedValueWhenPriceIsFound() {
        var givenPriceInfoApplicationDateQuery = new PriceInfoApplicationDateQuery(1, 1, LocalDateTime.now());
        var returnedPriceInfo = getDefaultPriceInfo();
        var expectedPriceResponse = getPriceResponseFromPriceInfo(returnedPriceInfo);

        when(priceRepository.findPriceInfoByApplicationDate(
                        givenPriceInfoApplicationDateQuery.brandId(),
                        givenPriceInfoApplicationDateQuery.productId(),
                        givenPriceInfoApplicationDateQuery.applicationDate()
                )
        ).thenReturn(Optional.of(returnedPriceInfo));

        var obtainedValue = findPriceInfoUseCase.findPriceInfoByApplicationDate(givenPriceInfoApplicationDateQuery);
        assertEquals(expectedPriceResponse, obtainedValue,"should be the expected price response");
    }

    private PriceInfo getDefaultPriceInfo(){
       return new PriceInfo(
               1,
               1,
               1,
               LocalDateTime.now(),
               LocalDateTime.now().plus(Period.ofDays(3)),
               BigDecimal.TEN,
               "EUR"
       );
    }

    private PriceInfoResponse getPriceResponseFromPriceInfo(PriceInfo priceInfo){
        return new PriceInfoResponse(
                priceInfo.brandId(),
                priceInfo.productId(),
                priceInfo.priceList(),
                priceInfo.priceStartAt(),
                priceInfo.priceEndsAt(),
                priceInfo.price(),
                priceInfo.currency()
        );
    }
}