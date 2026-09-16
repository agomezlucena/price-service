package io.github.agomezlucena.priceservice.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceInfoResponse(
        int brandId,
        int productId,
        int priceList,
        LocalDateTime priceStartAt,
        LocalDateTime priceEndsAt,
        BigDecimal price,
        String currency
) {

}
