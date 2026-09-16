package io.github.agomezlucena.priceservice.application;

import io.github.agomezlucena.priceservice.domain.PriceRepository;

public class FindPriceInfoUseCase implements PriceInfoFinder {
    private final PriceRepository priceRepository;

    public FindPriceInfoUseCase(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceInfoResponse findPriceInfoByApplicationDate(PriceInfoApplicationDateQuery query) {
        return null;
    }
}
