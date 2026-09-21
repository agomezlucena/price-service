package io.github.agomezlucena.priceservice.domain.criteria;

/**
 * Defines the sorting direction to be applied when ordering price information results.
 * This enumeration provides two possible values: ascending (ASC) and descending (DESC).
 *
 * <ul>
 *   <li>ASC: Indicates that results should be sorted in ascending order.</li>
 *   <li>DESC: Indicates that results should be sorted in descending order.</li>
 * </ul>
 *
 * The sorting direction is typically used in conjunction with {@code PriceInfoQueryField}
 * to specify the field and the order in which results should be presented.
 */
public enum PriceInfoSortDirection {
    ASC,
    DESC;

    /**
     * Parses a string value to determine the corresponding {@code PriceInfoSortDirection}.
     * If the input is null or does not match any known descending values, the default sort direction is ascending.
     *
     * @param value the string input to be parsed, representing the desired sort direction.
     *              Valid descending values include "DESC" and "DESCENDING" (case-insensitive).
     *              Any other input, including {@code null}, will default to ascending.
     *
     * @return the {@code PriceInfoSortDirection} corresponding to the input value.
     *         Returns {@code DESC} for valid descending values, or {@code ASC} otherwise.
     */
    public static PriceInfoSortDirection fromString(String value) {
        if (value == null) {
            return ASC;
        }
        return switch (value.trim().toUpperCase()) {
            case "DESC", "DESCENDING" -> DESC;
            default -> ASC;
        };
    }
}
