package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.criteria.CriterionComparator;
import io.github.agomezlucena.priceservice.domain.criteria.InvalidPriceCriteriaException;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQuery;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortCriterion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.LAST_UPDATE_BY;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.PRIORITY;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.DESC;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PriceSqlRepositoryItTest {
    @Autowired
    PriceSqlRepository priceSqlRepository;

    @Test
    void shouldReturnTheExpectedPriceInfoWhenQueryingByCriteria() {
        var givenApplicationDate = LocalDateTime.of(2020, 6, 14, 17, 0, 0);
        var expectedValue = new PriceInfo(
                1,
                35455,
                2,
                LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30, 0),
                BigDecimal.valueOf(25.45),
                "EUR"
        );

        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, givenApplicationDate)
                .withEndDate(CriterionComparator.GREATER_THAN_OR_EQUAL, givenApplicationDate)
                .orderBy(
                        PriceInfoSortCriterion.of(PRIORITY, DESC),
                        PriceInfoSortCriterion.of(LAST_UPDATE_BY, DESC)
                )
                .limit(1)
                .build();

        var obtainedValue = priceSqlRepository.findPriceInfoByCriteria(criteria);

        assertThat(obtainedValue)
                .describedAs("should return the price info matching the criteria")
                .contains(expectedValue);
    }

    @Test
    void shouldReturnEmptyOptionalWhenQueryingByCriteriaAndNotFound() {
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(99999)
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, LocalDateTime.now())
                .withEndDate(CriterionComparator.GREATER_THAN_OR_EQUAL, LocalDateTime.now())
                .orderBy(
                        PriceInfoSortCriterion.of(PRIORITY, DESC),
                        PriceInfoSortCriterion.of(LAST_UPDATE_BY, DESC)
                )
                .limit(1)
                .build();

        var obtainedValue = priceSqlRepository.findPriceInfoByCriteria(criteria);

        assertThat(obtainedValue)
                .describedAs("the obtained value should be empty because product does not exist")
                .isEmpty();
    }

    @Test
    void shouldThrowInvalidPriceCriteriaExceptionWhenCriteriaIsNotForSingleResult() {
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .limit(2)
                .build();

        assertThatThrownBy(() -> priceSqlRepository.findPriceInfoByCriteria(criteria))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("this method allow to extract only one result");
    }

    @Test
    void shouldThrowInvalidPriceCriteriaExceptionWhenCriteriaHasNoLimit() {
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .build();

        assertThatThrownBy(() -> priceSqlRepository.findPriceInfoByCriteria(criteria))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("this method allow to extract only one result");
    }
}