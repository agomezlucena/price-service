package io.github.agomezlucena.priceservice.domain.criteria;

public record PriceInfoSortCriterion(PriceInfoQueryField field, PriceInfoSortDirection direction) {
    public PriceInfoSortCriterion {
        if (field == null) {
            throw new InvalidPriceCriteriaException("field cannot be null");
        }
        if (direction == null) {
            throw new InvalidPriceCriteriaException("direction cannot be null");
        }
    }

    public static PriceInfoSortCriterion of(PriceInfoQueryField field, PriceInfoSortDirection direction) {
        return new PriceInfoSortCriterion(field, direction);
    }
}
