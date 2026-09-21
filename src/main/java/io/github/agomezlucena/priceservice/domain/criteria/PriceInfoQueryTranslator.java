package io.github.agomezlucena.priceservice.domain.criteria;

/**
 * Converts a {@link PriceInfoQuery} domain object into the desired representation
 * specified by the generic type {@code T}.
 * <p>
 * Implementations of this interface are expected to provide logic to translate
 * the query object into a specific format that can be processed to retrieve price information.
 *
 * @param <T> the type of the translated representation.
 */
public interface PriceInfoQueryTranslator <T> {
    /**
     * Translates the given {@code PriceInfoQuery} into a representation defined by the generic type {@code T}.
     * Implementations are responsible for converting the query object into a format suitable for further
     * processing, such as a SQL query, an HTTP request body, or another application-specific representation.
     *
     * @param query the {@code PriceInfoQuery} instance to be translated. Must contain valid filtering
     *              and sorting criteria, and optionally, a limit on result size.
     * @return the translated representation of the given {@code PriceInfoQuery}, formatted according
     *         to the requirements of the implementing class.
     */
    T translate(PriceInfoQuery query);
}
