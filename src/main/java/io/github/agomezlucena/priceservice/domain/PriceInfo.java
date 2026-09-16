package io.github.agomezlucena.priceservice.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceInfo(
        int brandId,
        int productId,
        int priceList,
        LocalDateTime priceStartAt,
        LocalDateTime priceEndsAt,
        BigDecimal price,
        String currency
) {
}
