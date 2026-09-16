package io.github.agomezlucena.priceservice.infrastructure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PricesRestControllerItTest {
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "Test {index}: brandId={0}, productId={1}, appDate={2} -> chargeId={3}, price={4}")
    @CsvSource({
            "1, 35455, 2020-06-14T10:00:00Z, 1, 35.50, EUR, 2020-06-14T00:00:00Z, 2020-12-31T23:59:59Z",
            "1, 35455, 2020-06-14T16:00:00Z, 2, 25.45, EUR, 2020-06-14T15:00:00Z, 2020-06-14T18:30:00Z",
            "1, 35455, 2020-06-14T21:00:00Z, 1, 35.50, EUR, 2020-06-14T00:00:00Z, 2020-12-31T23:59:59Z",
            "1, 35455, 2020-06-15T10:00:00Z, 3, 30.50, EUR, 2020-06-15T00:00:00Z, 2020-06-15T11:00:00Z",
            "1, 35455, 2020-06-15T21:00:00Z, 4, 38.95, EUR, 2020-06-15T16:00:00Z, 2020-12-31T23:59:59Z"
    })
    void shouldReturnTheExpectedPriceForGivenApplicationDate(
            int brandId,
            int productId,
            String applicationDate,
            int expectedChargeId,
            double expectedPrice,
            String expectedCurrency,
            String expectedPriceStartAt,
            String expectedPriceEndsAt
    ) throws Exception {
        mockMvc.perform(
                get("/api/v1/products/{productId}/prices", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Brand-Id", brandId)
                        .queryParam("applicationDate", applicationDate)
        ).andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.brand_id").value(brandId))
                .andExpect(jsonPath("$.product_id").value(productId))
                .andExpect(jsonPath("$.charge_id").value(expectedChargeId))
                .andExpect(jsonPath("$.price").value(expectedPrice))
                .andExpect(jsonPath("$.currency").value(expectedCurrency))
                .andExpect(jsonPath("$.price_start_at").value(expectedPriceStartAt))
                .andExpect(jsonPath("$.price_ends_at").value(expectedPriceEndsAt));
    }

    @Test
    void shouldReturnTheExpectedErrorCodeWhenProductDoesNotExists() throws Exception {
        mockMvc.perform(
                get("/api/v1/products/{productId}/prices", "80")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Brand-Id",1)
                        .queryParam("applicationDate", DateTimeFormatter.ISO_DATE_TIME.format(OffsetDateTime.now())
                )
        ).andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.title").value("Price not found"))
                .andExpect(jsonPath("$.detail").value("Price was not found"))
                .andExpect(jsonPath("$.instance").value("/api/v1/products/80/prices"));
    }


}
