package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.prices.openapi.generated.api.PriceQueryApi;
import io.github.agomezlucena.prices.openapi.generated.model.PriceResponse;
import io.github.agomezlucena.priceservice.domain.PriceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceRestController implements PriceQueryApi {

    @Override
    public ResponseEntity<PriceResponse> getPrice(Integer productId, Integer xBrandId, String applicationDate) {
        throw new PriceNotFoundException();
    }
}
