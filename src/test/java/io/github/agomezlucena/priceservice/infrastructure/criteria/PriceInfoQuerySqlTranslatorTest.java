package io.github.agomezlucena.priceservice.infrastructure.criteria;

import io.github.agomezlucena.priceservice.domain.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.*;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.ASC;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.DESC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PriceInfoQuerySqlTranslatorTest {
    @InjectMocks
    private PriceInfoCriteriaSqlTranslator translator;

    @Test
    void shouldTranslateFullCriteriaToExpectedSafeSql() {
        var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL, applicationDate)
                .withEndDate(CriterionComparator.GREATER_THAN_OR_EQUAL, applicationDate)
                .orderBy(
                        PriceInfoSortCriterion.of(PRIORITY, DESC),
                        PriceInfoSortCriterion.of(LAST_UPDATE, DESC)
                )
                .limit(1)
                .build();

        PriceInfoCriteriaSqlQuery query = translator.translate(criteria);

        var expectedSql = """
                select brand_id, product_id, price_list, start_date, end_date, price, currency
                from prices
                where brand_id = :brandId and
                      product_id = :productId and
                      start_date <= :startDate and
                      end_date >= :endDate
                order by priority desc, last_update desc""";

        assertThat(query.sql()).isEqualTo(expectedSql);
        assertThat(query.parameters())
                .containsEntry("brandId", 1)
                .containsEntry("productId", 35455)
                .containsEntry("startDate", applicationDate)
                .containsEntry("endDate", applicationDate);
    }

    @Test
    void shouldTranslateCustomOrderCriteria() {
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .orderBy(new PriceInfoSortCriterion(PRICE, ASC))
                .limit(5)
                .build();

        PriceInfoCriteriaSqlQuery query = translator.translate(criteria);

        assertThat(query.sql())
                .containsIgnoringCase("order by price asc")
                .doesNotContainIgnoringCase("limit");
        assertThat(query.parameters())
                .containsEntry("brandId", 1)
                .containsEntry("productId", 35455);
    }

    @Test
    void shouldOmitOrderByWhenNoOrderCriteriaProvided() {
        var criteria = PriceInfoQuery.builder()
                .brandId(1)
                .productId(35455)
                .limit(null)
                .build();

        PriceInfoCriteriaSqlQuery query = translator.translate(criteria);

        assertThat(query.sql())
                .doesNotContainIgnoringCase("order by")
                .doesNotContainIgnoringCase("limit");
        assertThat(query.parameters())
                .containsEntry("brandId", 1)
                .containsEntry("productId", 35455);
    }

    @Test
    void shouldTranslateAllSupportedOrderFieldsCorrectly() {
        for (PriceInfoQueryField field : PriceInfoQueryField.values()) {
            var criteria = PriceInfoQuery.builder()
                    .brandId(1)
                    .orderBy(new PriceInfoSortCriterion(field, DESC))
                    .build();

            PriceInfoCriteriaSqlQuery query = translator.translate(criteria);
            var expectedColumn = field.name().toLowerCase();

            assertThat(query.sql())
                    .containsIgnoringCase("order by " + expectedColumn + " desc");
        }
    }

    @Test
    void shouldThrowInvalidPriceCriteriaExceptionWhenCriteriaIsNull() {
        assertThatThrownBy(() -> translator.translate(null))
                .isInstanceOf(InvalidPriceCriteriaException.class)
                .hasMessage("PriceInfoCriteria cannot be null");
    }

    @Test
    void shouldTranslateAllComparatorsCorrectly() {
        var criteria = new PriceInfoQuery(
                java.util.List.of(
                        new PriceInfoCriterion(PRICE, CriterionComparator.EQUALS, 10),
                        new PriceInfoCriterion(PRIORITY, CriterionComparator.GREATER_THAN, 1),
                        new PriceInfoCriterion(PRICE_LIST, CriterionComparator.LESS_THAN, 5),
                        new PriceInfoCriterion(START_DATE, CriterionComparator.GREATER_THAN_OR_EQUAL, LocalDateTime.MIN),
                        new PriceInfoCriterion(END_DATE, CriterionComparator.LESS_THAN_OR_EQUAL, LocalDateTime.MAX),
                        new PriceInfoCriterion(BRAND_ID, null, 1)
                ),
                java.util.List.of(),
                null
        );

        var query = translator.translate(criteria);

        assertThat(query.sql())
                .contains("price = :price")
                .contains("priority > :priority")
                .contains("price_list < :priceList")
                .contains("start_date >= :startDate")
                .contains("end_date <= :endDate")
                .contains("brand_id = :brandId");
    }
}
