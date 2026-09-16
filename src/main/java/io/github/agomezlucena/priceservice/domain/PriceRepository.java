package io.github.agomezlucena.priceservice.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<PriceInfo> findPriceInfoByApplicationDate(int brandId, int productId, LocalDateTime applicationDate);
}
