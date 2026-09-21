package io.github.agomezlucena.priceservice.application;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.PriceRepository;
import io.github.agomezlucena.priceservice.domain.criteria.CriterionComparator;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQuery;
import io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortCriterion;

import java.util.Optional;

import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.LAST_UPDATE_BY;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoQueryField.PRIORITY;
import static io.github.agomezlucena.priceservice.domain.criteria.PriceInfoSortDirection.DESC;

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
     */
    @Override
    public Optional<PriceInfoResponse> findPriceInfoByApplicationDate(PriceInfoApplicationDateQuery query) {
        var criteria = PriceInfoQuery.builder()
                .brandId(query.brandId())
                .productId(query.productId())
                .withStartDate(CriterionComparator.LESS_THAN_OR_EQUAL,query.applicationDate())
                .withEndDate(CriterionComparator.GREATER_THAN_OR_EQUAL,query.applicationDate())
                .orderBy(
                        PriceInfoSortCriterion.of(PRIORITY, DESC),
                        PriceInfoSortCriterion.of(LAST_UPDATE_BY, DESC)
                )
                .limit(1)
                .build();

        return priceRepository.findPriceInfoByCriteria(criteria)
                .map(this::mapPriceInfo);
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
