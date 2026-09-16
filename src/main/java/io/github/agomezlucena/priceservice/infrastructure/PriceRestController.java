package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.prices.openapi.generated.api.PriceQueryApi;
import io.github.agomezlucena.prices.openapi.generated.model.PriceResponse;
import io.github.agomezlucena.priceservice.application.PriceInfoApplicationDateQuery;
import io.github.agomezlucena.priceservice.application.PriceInfoFinder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@RestController
public class PriceRestController implements PriceQueryApi {
    private final PriceInfoFinder priceInfoFinder;
    private final PriceInfoResponseMapper priceInfoResponseMapper;

    
    public PriceRestController(PriceInfoFinder priceInfoFinder, PriceInfoResponseMapper priceInfoResponseMapper) {
        this.priceInfoFinder = priceInfoFinder;
        this.priceInfoResponseMapper = priceInfoResponseMapper;
    }

    @Override
    public ResponseEntity<PriceResponse> getPrice(Integer productId, Integer xBrandId, OffsetDateTime applicationDate) {
        var priceInfoQuery = new PriceInfoApplicationDateQuery(
                xBrandId,
                productId,
                applicationDate.atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime()
        );

        var obtainedPriceInfo = priceInfoFinder.findPriceInfoByApplicationDate(priceInfoQuery);
        return ResponseEntity.ok()
                .body(priceInfoResponseMapper.toPriceResponse(obtainedPriceInfo));
    }
}
