package io.github.agomezlucena.priceservice.domain;

import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQuery;

import java.util.Optional;

public interface PriceRepository {
    /**
     * Searches for price information based on the provided criteria.
     *
     * This method allows querying for specific price details using a set of criteria,
     * including filtering conditions, sorting preferences, and optional limits.
     *
     * @param query an instance of {@code PriceInfoQuery} containing filtering criteria,
     *              sorting rules, and an optional limit for the query result
     * @return an {@code Optional} containing {@code PriceInfo} if a matching result is found;
     *         otherwise, an empty {@code Optional}
     */
    Optional<PriceInfo> findPriceInfoByCriteria(PriceInfoQuery query);
}
