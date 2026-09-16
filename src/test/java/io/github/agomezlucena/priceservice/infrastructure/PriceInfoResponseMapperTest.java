package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.prices.openapi.generated.model.PriceResponse;
import io.github.agomezlucena.priceservice.application.PriceInfoResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class PriceInfoResponseMapperTest {

    private final PriceInfoResponseMapper mapper = Mappers.getMapper(PriceInfoResponseMapper.class);

    @Test
    void shouldMapPriceInfoResponseToPriceResponse() {
        LocalDateTime start = LocalDateTime.of(2026, 6, 14, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 12, 31, 23, 59, 59);

        PriceInfoResponse input = new PriceInfoResponse(
                1,
                35455,
                2,
                start,
                end,
                new BigDecimal("25.45"),
                "EUR"
        );

        PriceResponse result = mapper.toPriceResponse(input);

        assertNotNull(result);
        assertEquals(1, result.getBrandId());
        assertEquals(35455, result.getProductId());
        assertEquals(2, result.getChargeId());
        assertEquals(start.atOffset(ZoneOffset.UTC), result.getPriceStartAt());
        assertEquals(end.atOffset(ZoneOffset.UTC), result.getPriceEndsAt());
        assertEquals(new BigDecimal("25.45"), result.getPrice());
        assertEquals("EUR", result.getCurrency());
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toPriceResponse(null));
    }
}
