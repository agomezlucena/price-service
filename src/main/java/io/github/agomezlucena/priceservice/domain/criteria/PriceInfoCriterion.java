package io.github.agomezlucena.priceservice.domain.criteria;

public record PriceInfoCriterion(
        PriceInfoQueryField field,
        CriterionComparator comparator,
        Object value
){
    public static PriceInfoCriterion fieldEqualsTo(PriceInfoQueryField field, Object value) {
        return new PriceInfoCriterion(field, CriterionComparator.EQUALS, value);
    }
}
