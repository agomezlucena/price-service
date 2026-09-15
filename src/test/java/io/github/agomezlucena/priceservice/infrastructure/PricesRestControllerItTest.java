package io.github.agomezlucena.priceservice.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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

    @Test
    void shouldReturnTheExpectedErrorCodeWhenProductDoesNotExists() throws Exception {
        mockMvc.perform(
                get("/api/v1/products/{productId}/prices", "80")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Brand-Id",1)
                        .queryParam("applicationDate", DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
                )
        ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.title").value("Price not found"))
                .andExpect(jsonPath("$.detail").value("Price was not found"))
                .andExpect(jsonPath("$.instance").value("/api/v1/products/80/prices"));
    }
}
