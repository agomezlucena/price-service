package io.github.agomezlucena.priceservice.domain.criteria;

/**
 * Represents a filtering criterion for querying price information.
 * Each criterion is composed of a query field, a comparator, and a value
 * that define a specific condition to be matched in the query.
 *
 * @param field      the {@code PriceInfoQueryField} that specifies the field to filter by.
 * @param comparator the {@code CriterionComparator} that specifies the comparison operation
 *                   to apply between the field and the value.
 * @param value      the value to compare the field against based on the comparator. It can be of
 *                   various types, depending on the particular field and comparator being used.
 */
public record PriceInfoCriterion(
        PriceInfoQueryField field,
        CriterionComparator comparator,
        Object value
){
    public static PriceInfoCriterion fieldEqualsTo(PriceInfoQueryField field, Object value) {
        return new PriceInfoCriterion(field, CriterionComparator.EQUALS, value);
    }
}
