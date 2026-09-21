package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.prices.openapi.generated.api.PriceQueryApi;
import io.github.agomezlucena.prices.openapi.generated.model.PriceResponse;
import io.github.agomezlucena.priceservice.application.PriceInfoApplicationDateQuery;
import io.github.agomezlucena.priceservice.application.PriceInfoFinder;
import io.github.agomezlucena.priceservice.domain.PriceNotFoundException;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * PriceRestController serves as the REST controller for handling HTTP requests related to price information.
 * It implements the {@code PriceQueryApi} interface to provide the endpoint for querying prices based on
 * brand, product, and application date.
 *</br>
 * This controller relies on {@code PriceInfoFinder} for retrieving the price data, and
 * {@code PriceInfoResponseMapper} for mapping the domain-specific price information into a response format.
 */
@RestController
public class PriceRestController implements PriceQueryApi {
    private final PriceInfoFinder priceInfoFinder;
    private final PriceInfoResponseMapper priceInfoResponseMapper;

    
    public PriceRestController(PriceInfoFinder priceInfoFinder, PriceInfoResponseMapper priceInfoResponseMapper) {
        this.priceInfoFinder = priceInfoFinder;
        this.priceInfoResponseMapper = priceInfoResponseMapper;
    }

    /**
     * Retrieves the price information for a specific product and brand based on the provided application date.
     *
     * @param productId the identifier of the product for which the price information is requested
     * @param xBrandId the identifier of the brand associated with the product
     * @param applicationDate the date and time to be used for determining the applicable price information
     * @return a {@code ResponseEntity} containing the price details as a {@code PriceResponse} object
     */
    @Override
    @Timed("price.rest.query.timespent")
    public ResponseEntity<PriceResponse> getPrice(Integer productId, Integer xBrandId, OffsetDateTime applicationDate) {
        var priceInfoQuery = new PriceInfoApplicationDateQuery(
                xBrandId,
                productId,
                applicationDate.atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime()
        );

        return priceInfoFinder.findPriceInfoByApplicationDate(priceInfoQuery)
                .map(priceInfoResponseMapper::toPriceResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(PriceNotFoundException::new);
    }
}
