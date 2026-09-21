package io.github.agomezlucena.priceservice.domain.criteria;

import org.junit.jupiter.api.Test;

import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.ASC;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.DESC;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.PRICE;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.PRIORITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceInfoSortCriterionTest {

    @Test
    void shouldCreateOrderCriteriaViaConstructor() {
        var order = new PriceInfoSortCriterion(PRIORITY, DESC);

        assertThat(order.field()).isEqualTo(PRIORITY);
        assertThat(order.direction()).isEqualTo(DESC);
    }

    @Test
    void shouldCreateOrderCriteriaViaFactoryMethod() {
        var order = PriceInfoSortCriterion.of(PRICE, ASC);

        assertThat(order.field()).isEqualTo(PRICE);
        assertThat(order.direction()).isEqualTo(ASC);
    }

    @Test
    void shouldRejectNullField() {
        assertThatThrownBy(() -> new PriceInfoSortCriterion(null, ASC))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("field cannot be null");

        assertThatThrownBy(() -> PriceInfoSortCriterion.of(null, ASC))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("field cannot be null");
    }

    @Test
    void shouldRejectNullDirection() {
        assertThatThrownBy(() -> new PriceInfoSortCriterion(PRIORITY, null))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("direction cannot be null");

        assertThatThrownBy(() -> PriceInfoSortCriterion.of(PRIORITY, null))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("direction cannot be null");
    }

    @Test
    void shouldHaveProperEqualsAndHashCodeAndToString() {
        var order1 = PriceInfoSortCriterion.of(PRIORITY, DESC);
        var order2 = new PriceInfoSortCriterion(PRIORITY, DESC);
        var differentOrder = PriceInfoSortCriterion.of(PRIORITY, ASC);

        assertThat(order1).isEqualTo(order2);
        assertThat(order1.hashCode()).isEqualTo(order2.hashCode());
        assertThat(order1).isNotEqualTo(differentOrder);
        assertThat(order1.toString()).contains("PRIORITY", "DESC");
    }
}
