package io.github.agomezlucena.priceservice.domain.criteria;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.ASC;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.DESC;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.fromString;
import static org.assertj.core.api.Assertions.assertThat;

class PriceInfoSortDirectionTest {

    @ParameterizedTest
    @ValueSource(strings = {"desc", "DESC", "descending", "DESCENDING"})
    void shouldParseDescendingDirections(String direction) {
        assertThat(fromString(direction)).isEqualTo(DESC);
    }

    @ParameterizedTest
    @ValueSource(strings = {"asc", "ASC", "ascending", "other"})
    @NullAndEmptySource
    void shouldDefaultOrParseAscendingDirections(String direction) {
        assertThat(fromString(direction)).isEqualTo(ASC);
    }
}
