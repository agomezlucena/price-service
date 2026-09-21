package io.github.agomezlucena.priceservice.application;

import java.util.Optional;

/**
 * Interface for retrieving price information based on specific criteria such as brand, product,
 * and application date.
 * Provides a method for querying detailed price information, including price list, validity
 * period, value, and currency, for given parameters.
 */
public interface PriceInfoFinder {
    /**
     * Retrieves price information based on the provided application date, brand, and product details.
     *
     * @param query the query containing the brand ID, product ID, and the application date for which
     *              the price information is requested
     * @return a {@code PriceInfoResponse} containing the relevant price details, including
     *         the price list, price validity period, price value, and currency
     *
     * @throws io.github.agomezlucena.priceservice.domain.PriceNotFoundException when no are found for that query.
     */
    Optional<PriceInfoResponse> findPriceInfoByApplicationDate(PriceInfoApplicationDateQuery query);
}
