package io.github.agomezlucena.priceservice.infrastructure.criteria;

import java.util.Map;

/**
 * Represents a translated SQL query with its associated named parameters.
 *
 * @param sql the parameterized SQL query string
 * @param parameters the map of named parameter names to values
 */
public record PriceInfoCriteriaSqlQuery(String sql, Map<String, Object> parameters) {

    public PriceInfoCriteriaSqlQuery {
        parameters = parameters != null ? Map.copyOf(parameters) : Map.of();
    }
}
