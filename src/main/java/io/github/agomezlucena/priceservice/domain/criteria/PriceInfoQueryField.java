package io.github.agomezlucena.priceservice.domain.criteria;

/**
 * Represents the fields that can be used as query parameters when constructing
 * criteria for retrieving price-related information in the system.
 *
 * Each field corresponds to a specific property of a price entry and can be used
 * for filtering or sorting the results of a query. These fields serve as keys
 * to define the conditions in {@code PriceInfoCriterion} or as sorting fields in
 * {@code PriceInfoSortCriterion}.
 *
 * Enumeration elements:
 *
 * - PRIORITY: Represents the priority level of a price entry.
 * - LAST_UPDATE_BY: Refers to the identifier of the entity or user who last updated the entry.
 * - LAST_UPDATE: Refers to the timestamp of the last update to the entry.
 * - START_DATE: The start date of the validity of the price entry.
 * - END_DATE: The end date of the validity of the price entry.
 * - PRICE: The price value of the entry.
 * - PRICE_LIST: Represents the identifier of the price list to which the entry belongs.
 * - BRAND_ID: The identifier of the brand associated with the price entry.
 * - PRODUCT_ID: The identifier of the product associated with the price entry.
 */
public enum PriceInfoQueryField {
    PRIORITY,
    LAST_UPDATE_BY,
    LAST_UPDATE,
    START_DATE,
    END_DATE,
    PRICE,
    PRICE_LIST,
    BRAND_ID,
    PRODUCT_ID
}
