package io.github.agomezlucena.priceservice.domain.criteria;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static io.github.agomezlucena.priceservice.domain.criteria.CriterionComparator.EQUALS;
import static io.github.agomezlucena.priceservice.domain.criteria.CriterionComparator.LESS_THAN_OR_EQUAL;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.BRAND_ID;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.START_DATE;
import static org.assertj.core.api.Assertions.assertThat;

class PriceInfoCriterionTest {

    @Test
    void shouldCreatePriceInfoCriteriaWithGivenValues() {
        var criteria = new PriceInfoCriterion(BRAND_ID, EQUALS, 1);

        assertThat(criteria.field()).isEqualTo(BRAND_ID);
        assertThat(criteria.comparator()).isEqualTo(EQUALS);
        assertThat(criteria.value()).isEqualTo(1);
    }

    @Test
    void shouldSupportDifferentComparatorsAndValues() {
        var date = LocalDateTime.of(2020, 6, 14, 10, 0);
        var criteria = new PriceInfoCriterion(START_DATE, LESS_THAN_OR_EQUAL, date);

        assertThat(criteria.field()).isEqualTo(START_DATE);
        assertThat(criteria.comparator()).isEqualTo(LESS_THAN_OR_EQUAL);
        assertThat(criteria.value()).isEqualTo(date);
    }

    @Test
    void shouldHaveProperEqualsAndHashCodeAndToString() {
        var criteria1 = new PriceInfoCriterion(BRAND_ID, EQUALS, 1);
        var criteria2 = new PriceInfoCriterion(BRAND_ID, EQUALS, 1);
        var differentCriteria = new PriceInfoCriterion(BRAND_ID, EQUALS, 2);

        assertThat(criteria1).isEqualTo(criteria2);
        assertThat(criteria1.hashCode()).isEqualTo(criteria2.hashCode());
        assertThat(criteria1).isNotEqualTo(differentCriteria);
        assertThat(criteria1.toString()).contains("BRAND_ID", "EQUALS", "1");
    }
}
