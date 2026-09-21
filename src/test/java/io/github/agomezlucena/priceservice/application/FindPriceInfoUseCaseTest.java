package io.github.agomezlucena.priceservice.application;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.PriceRepository;
import io.github.agomezlucena.priceservice.domain.criteria.CriterionComparator;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQuery;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortCriterion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.LAST_UPDATE_BY;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.PRIORITY;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.DESC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPriceInfoUseCaseTest {
    @Mock
    private PriceRepository priceRepository;
    @InjectMocks
    private FindPriceInfoUseCase findPriceInfoUseCase;

    @Test
    void shouldReturnAnEmptyOptionalIfNoPriceIsFound() {
        var givenPriceInfoApplicationDateQuery = new PriceInfoApplicationDateQuery(80, 80, LocalDateTime.now());
        var expectedCriteria = getApplicationDateCriteria(givenPriceInfoApplicationDateQuery);

        when(priceRepository.findPriceInfoByCriteria(expectedCriteria)).thenReturn(Optional.empty());

        var obtainedValue = findPriceInfoUseCase.findPriceInfoByApplicationDate(givenPriceInfoApplicationDateQuery);

        assertThat(obtainedValue)
                .describedAs("should not found any object for the given criteria")
                .isEmpty();
    }

    @Test
    void shouldReturnTheExpectedValueWhenPriceIsFound() {
        var givenPriceInfoApplicationDateQuery = new PriceInfoApplicationDateQuery(1, 1, LocalDateTime.now());
        var returnedPriceInfo = getDefaultPriceInfo();
        var expectedCriteria = getApplicationDateCriteria(givenPriceInfoApplicationDateQuery);
        var expectedPriceResponse = getPriceResponseFromPriceInfo(returnedPriceInfo);

        when(priceRepository.findPriceInfoByCriteria(expectedCriteria)).thenReturn(Optional.of(returnedPriceInfo));

        var obtainedValue = findPriceInfoUseCase.findPriceInfoByApplicationDate(givenPriceInfoApplicationDateQuery);

        assertThat(obtainedValue)
                .describedAs("should contains the expected value")
                .hasValue(expectedPriceResponse);
    }

    private PriceInfo getDefaultPriceInfo() {
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

    private PriceInfoResponse getPriceResponseFromPriceInfo(PriceInfo priceInfo) {
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

    private PriceInfoQuery getApplicationDateCriteria(PriceInfoApplicationDateQuery query) {
        return PriceInfoQuery.builder()
                .brandId(query.brandId())
                .productId(query.productId())
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, query.applicationDate())
                .withEndDate(CriterionComparator.GREATER_THAN_OR_EQUAL, query.applicationDate())
                .orderBy(
                        PriceInfoSortCriterion.of(PRIORITY, DESC),
                        PriceInfoSortCriterion.of(LAST_UPDATE_BY, DESC)
                )
                .limit(1)
                .build();
    }
}