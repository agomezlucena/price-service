package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.prices.openapi.generated.model.PriceResponse;
import io.github.agomezlucena.priceservice.application.PriceInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceInfoResponseMapper {

    @Mapping(target = "chargeId", source = "priceList")
    PriceResponse toPriceResponse(PriceInfoResponse priceInfoResponse);

    default OffsetDateTime map(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(ZoneOffset.UTC) : null;
    }
}
