package io.github.agomezlucena.priceservice.application;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.PriceNotFoundException;
import io.github.agomezlucena.priceservice.domain.PriceRepository;

/**
 * Use case implementation for finding price information based on application date, brand, and product.
 * This class interacts with the {@code PriceRepository} to retrieve price details and maps the
 * data to a {@code PriceInfoResponse}.
 *
 * Implements the {@code PriceInfoFinder} interface for handling price retrieval logic.
 */
public class FindPriceInfoUseCase implements PriceInfoFinder {
    private final PriceRepository priceRepository;

    public FindPriceInfoUseCase(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /**
     * Retrieves price information for a specific brand, product, and application date.
     *
     * The method fetches price details from the repository using the provided query parameters.
     * If no price information is found, it throws a {@code PriceNotFoundException}.
     *
     * @param query an instance of {@code PriceInfoApplicationDateQuery} containing the brand ID,
     *              product ID, and application date for which the price information is being queried
     * @return a {@code PriceInfoResponse} containing price details, including brand ID, product ID,
     *         price list, validity period, price value, and currency
     * @throws PriceNotFoundException if no price information is found for the given query parameters
     */
    @Override
    public PriceInfoResponse findPriceInfoByApplicationDate(PriceInfoApplicationDateQuery query) {
        return priceRepository.findPriceInfoByApplicationDate(
                    query.brandId(), query.productId(), query.applicationDate()
                ).map(this::mapPriceInfo)
                .orElseThrow(PriceNotFoundException::new);
    }

    private PriceInfoResponse mapPriceInfo(PriceInfo priceInfo) {
        return new PriceInfoResponse(
                priceInfo.brandId(),
                priceInfo.productId(),
                priceInfo.priceList(),
                priceInfo.priceStartAt(),
                priceInfo.priceEndsAt(),
                priceInfo.price(),
                priceInfo.currency()
        );
    }
}
