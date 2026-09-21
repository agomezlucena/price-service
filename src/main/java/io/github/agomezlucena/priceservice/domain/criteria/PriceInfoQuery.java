package io.github.agomezlucena.priceservice.domain.criteria;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a query used to search for price information based on a list of criteria,
 * sorting rules, and an optional limit on the number of results.
 */
public record PriceInfoQuery(
        List<PriceInfoCriterion> criteria,
        List<PriceInfoSortCriterion> priceInfoOrderCriteria,
        Integer limit
) {
    /**
     * Constructs a new {@code PriceInfoQuery} to encapsulate criteria, sorting rules, and result limit
     * for querying price information. This constructor enforces validation on the provided input
     * to ensure the query is both meaningful and well-formed.
     *
     * @param criteria                 the list of {@code PriceInfoCriterion} instances that define
     *                                 the filtering criteria for the query. Must not be null or empty.
     * @param priceInfoOrderCriteria   the list of {@code PriceInfoSortCriterion} instances that define
     *                                 the sorting order for the query results. If null, defaults to an empty list.
     * @param limit                    an optional integer specifying the maximum number of results.
     *                                 If not null, must be a value greater than 0.
     * @throws InvalidPriceCriteriaException if {@code criteria} is null or empty.
     * @throws InvalidPriceCriteriaException if {@code limit} is not null and is less than 1.
     */
    public PriceInfoQuery {
        criteria = Optional.ofNullable(criteria)
                .map(List::copyOf)
                .orElse(Collections.emptyList());

        priceInfoOrderCriteria = Optional.ofNullable(priceInfoOrderCriteria)
                .map(List::copyOf)
                .orElse(Collections.emptyList());

        if (criteria.isEmpty()) {
            throw new InvalidPriceCriteriaException("Invalid price info query the criteria are empty");
        }

        if (limit != null && limit < 1) {
            throw new InvalidPriceCriteriaException("Invalid price info query the criteria limit must greater than 0");
        }
    }

    /**
     * Creates a new {@code Builder} instance for constructing a {@code PriceInfoQuery}.
     *
     * @return a new {@code Builder} instance to configure and build a {@code PriceInfoQuery}.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<PriceInfoCriterion> criteria = new ArrayList<>();
        private final List<PriceInfoSortCriterion> priceInfoOrderCriteria = new ArrayList<>();
        private Integer limit;

        public Builder brandId(Integer brandId) {
            criteria.add(PriceInfoCriterion.fieldEqualsTo(PriceInfoQueryField.BRAND_ID, brandId));
            return this;
        }

        public Builder productId(Integer productId) {
            criteria.add(PriceInfoCriterion.fieldEqualsTo(PriceInfoQueryField.PRODUCT_ID, productId));
            return this;
        }

        public Builder withStartDate(CriterionComparator comparator, LocalDateTime startDate) {
            criteria.add(new PriceInfoCriterion(PriceInfoQueryField.START_DATE, comparator, startDate));
            return this;
        }

        public Builder withEndDate(CriterionComparator comparator, LocalDateTime endDate) {
            criteria.add(new PriceInfoCriterion(PriceInfoQueryField.END_DATE, comparator, endDate));
            return this;
        }

        public Builder orderBy(PriceInfoQueryField field, PriceInfoSortDirection sortDirection) {
            return this.orderBy(PriceInfoSortCriterion.of(field, sortDirection));
        }

        public Builder orderBy(PriceInfoSortCriterion... priceInfoOrderCriteria) {
            Collections.addAll(this.priceInfoOrderCriteria, priceInfoOrderCriteria);
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public PriceInfoQuery build() {
            return new PriceInfoQuery(criteria, priceInfoOrderCriteria, limit);
        }
    }
}
