package io.github.agomezlucena.priceservice.domain.criteria;

public enum PriceInfoSortDirection {
    ASC,
    DESC;

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
