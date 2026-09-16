package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.application.FindPriceInfoUseCase;
import io.github.agomezlucena.priceservice.application.PriceInfoFinder;
import io.github.agomezlucena.priceservice.domain.PriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Bean
    public PriceInfoFinder priceInfoFinder(PriceRepository priceRepository) {
        return new FindPriceInfoUseCase(priceRepository);
    }
}
