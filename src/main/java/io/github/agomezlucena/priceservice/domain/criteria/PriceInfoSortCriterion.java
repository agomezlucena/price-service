package io.github.agomezlucena.priceservice.domain.criteria;

/**
 * Represents a sorting criterion used for ordering price-related query results.
 * Each instance of this record is composed of a field to sort by and a sort direction.
 *
 * Instances of this record are immutable and ensure that the field and direction are
 * not null during initialization.
 *
 * @param field     the {@code PriceInfoQueryField} indicating the field by which
 *                  the sorting will be performed. Must not be null.
 * @param direction the {@code PriceInfoSortDirection} specifying the direction
 *                  (ascending or descending) of the sort. Must not be null.
 *
 * @throws InvalidPriceCriteriaException if {@code field} or {@code direction} is null.
 */
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
