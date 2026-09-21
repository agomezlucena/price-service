package io.github.agomezlucena.priceservice.domain.criteria;

/**
 * Represents the set of comparison operations that can be applied
 * to filter query criteria within the price information query functionality.
 *
 * <ul>
 *   <li>EQUALS: Checks if the field value is equal to the specified value.</li>
 *   <li>GREATER_THAN: Checks if the field value is greater than the specified value.</li>
 *   <li>LESS_THAN: Checks if the field value is less than the specified value.</li>
 *   <li>GREATER_THAN_OR_EQUAL: Checks if the field value is greater than or equal to the specified value.</li>
 *   <li>LESS_THAN_OR_EQUAL: Checks if the field value is less than or equal to the specified value.</li>
 * </ul>
 */
public enum CriterionComparator {
    EQUALS,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN_OR_EQUAL,
}
