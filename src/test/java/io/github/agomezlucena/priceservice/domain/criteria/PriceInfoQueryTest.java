package io.github.agomezlucena.priceservice.domain.criteria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.*;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.ASC;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.DESC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceInfoQueryTest {

    @Test
    void shouldBuildCriteriaWithAllFieldsUsingBuilder() {
        var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, applicationDate)
                .withEndDate(CriterionComparator.GREATER_THAN_OR_EQUAL, applicationDate)
                .orderBy(
                        PriceInfoSortCriterion.of(PRIORITY, DESC),
                        PriceInfoSortCriterion.of(LAST_UPDATE_BY, DESC)
                )
                .limit(1)
                .build();

        assertThat(criteria.criteria()).containsExactly(
                new PriceInfoCriterion(BRAND_ID, CriterionComparator.EQUALS, 1),
                new PriceInfoCriterion(PRODUCT_ID, CriterionComparator.EQUALS, 35455),
                new PriceInfoCriterion(START_DATE, CriterionComparator.LESS_THAN_OR_EQUAL, applicationDate),
                new PriceInfoCriterion(END_DATE, CriterionComparator.GREATER_THAN_OR_EQUAL, applicationDate)
        );
        assertThat(criteria.limit()).isEqualTo(1);
        assertThat(criteria.priceInfoOrderCriteria()).hasSize(2);
        assertThat(criteria.priceInfoOrderCriteria().get(0)).isEqualTo(PriceInfoSortCriterion.of(PRIORITY, DESC));
        assertThat(criteria.priceInfoOrderCriteria().get(1)).isEqualTo(PriceInfoSortCriterion.of(LAST_UPDATE_BY, DESC));
    }

    @Test
    void shouldSupportFluentOrderByAndEnumFields() {
        var applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, applicationDate)
                .orderBy(PriceInfoSortCriterion.of(PRICE, ASC))
                .orderBy(PriceInfoSortCriterion.of(START_DATE, DESC))
                .build();

        assertThat(criteria.criteria()).containsExactly(
                new PriceInfoCriterion(BRAND_ID, CriterionComparator.EQUALS, 1),
                new PriceInfoCriterion(PRODUCT_ID, CriterionComparator.EQUALS, 35455),
                new PriceInfoCriterion(START_DATE, CriterionComparator.LESS_THAN_OR_EQUAL, applicationDate)
        );

        assertThat(criteria.priceInfoOrderCriteria()).containsExactly(
                PriceInfoSortCriterion.of(PRICE, ASC),
                PriceInfoSortCriterion.of(START_DATE, DESC)
        );
    }

    @Test
    void shouldSupportAllOrderByConvenienceMethods() {
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .orderBy(PRICE, ASC)
                .orderBy(PRIORITY, DESC)
                .build();

        assertThat(criteria.priceInfoOrderCriteria()).containsExactly(
                new PriceInfoSortCriterion(PRICE, ASC),
                new PriceInfoSortCriterion(PRIORITY, DESC)
        );
    }


    @Test
    void shouldSupportSettingListOfOrderCriteria() {
        var sorting = List.of(
                PriceInfoSortCriterion.of(PRIORITY, DESC),
                PriceInfoSortCriterion.of(PRICE, ASC)
        );
        var criteria = PriceInfoQuery.builder()
                .brandId(2)
                .productId(99999)
                .orderBy(PriceInfoSortCriterion.of(PRIORITY, DESC))
                .orderBy(PriceInfoSortCriterion.of(PRICE, ASC))
                .build();

        assertThat(criteria.priceInfoOrderCriteria()).isEqualTo(sorting);
    }



    @Test
    void shouldHaveProperEqualsAndHashCodeAndToString() {
        var date = LocalDateTime.of(2020, 6, 14, 10, 0);
        var criteria1 = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, date)
                .orderBy(PriceInfoSortCriterion.of(PRIORITY, DESC))
                .build();

        var criteria2 = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, date)
                .orderBy(PriceInfoSortCriterion.of(PRIORITY, DESC))
                .build();

        assertThat(criteria1).isEqualTo(criteria2);
        assertThat(criteria1.hashCode()).isEqualTo(criteria2.hashCode());
        assertThat(criteria1.toString()).contains("BRAND_ID", "PRODUCT_ID");
    }


    @Test
    void shouldRejectNullFieldInBuilderOrderBy() {
        assertThatThrownBy(() -> PriceInfoQuery.builder().orderBy(null, ASC))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("field cannot be null");
    }

    @Test
    void shouldHandleNullCriteriaAndOrderCriteriaInConstructor() {
        assertThatThrownBy(() -> PriceInfoQuery.builder().build())
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("Invalid price info query the criteria are empty");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    void shouldThrowExceptionWhenLimitIsLessThanOne(int invalidLimit) {
        assertThatThrownBy(() -> PriceInfoQuery.builder()
                .brandId(1)
                .limit(invalidLimit)
                .build())
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("Invalid price info query the criteria limit must greater than 0");

        var validCriterion = List.of(PriceInfoCriterion.fieldEqualsTo(BRAND_ID, 1));
        assertThatThrownBy(() -> new PriceInfoQuery(validCriterion, List.of(), invalidLimit))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("Invalid price info query the criteria limit must greater than 0");
    }
}
