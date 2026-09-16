package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PriceSqlRepositoryItTest {
    @Autowired
    PriceSqlRepository priceSqlRepository;

    @Test
    void shouldReturnAnEmptyOptionalIfNoPriceInfoIsFound() {
        var obtainedValue = priceSqlRepository.findPriceInfoByApplicationDate(1, 1, LocalDateTime.now());
        assertThat(obtainedValue)
                .describedAs("the obtained value should be empty because it doesn't exists")
                .isEmpty();
    }

    @Test
    void shouldReturnTheExpectedPriceInfoIfExists() {
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

        var obtainedValue = priceSqlRepository.findPriceInfoByApplicationDate(1, 35455, givenApplicationDate);

        assertThat(obtainedValue)
                .describedAs("should return the price with maximum priority for that date of that product")
                .contains(expectedValue);
    }
}