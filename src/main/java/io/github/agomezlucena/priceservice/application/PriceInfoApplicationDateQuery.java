package io.github.agomezlucena.priceservice.application;

import java.time.LocalDateTime;

public record PriceInfoApplicationDateQuery(int brandId, int productId, LocalDateTime applicationDate) {
}
