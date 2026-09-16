package io.github.agomezlucena.priceservice.application;

import io.github.agomezlucena.priceservice.domain.PriceInfo;
import io.github.agomezlucena.priceservice.domain.PriceNotFoundException;
import io.github.agomezlucena.priceservice.domain.PriceRepository;

public class FindPriceInfoUseCase implements PriceInfoFinder {
    private final PriceRepository priceRepository;

    public FindPriceInfoUseCase(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceInfoResponse findPriceInfoByApplicationDate(PriceInfoApplicationDateQuery query) {
        return priceRepository.findPriceInfoByApplicationDate(
                    query.brandId(), query.productId(), query.applicationDate()
                ).map(this::mapPriceInfo)
                .orElseThrow(PriceNotFoundException::new);
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
