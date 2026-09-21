package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.domain.PriceNotFoundException;
import io.github.agomezlucena.priceservice.domain.criteria.InvalidPriceCriteriaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PriceControllerAdviceItTest {

    private PriceControllerAdvice controllerAdvice;
    private MockMvc mockMvc;

    @RestController
    static class TestErrorController {
        @GetMapping("/test/price-not-found")
        public void throwPriceNotFound() {
            throw new PriceNotFoundException();
        }

        @GetMapping("/test/unknown-error")
        public void throwUnknownError() {
            throw new RuntimeException("Database connection failure");
        }

        @GetMapping("/test/invalid-criteria")
        public void throwInvalidPriceCriteria() {
            throw new InvalidPriceCriteriaException("Invalid price criteria");
        }
    }

    @BeforeEach
    void setUp() {
        controllerAdvice = new PriceControllerAdvice();
        mockMvc = MockMvcBuilders.standaloneSetup(new TestErrorController())
                .setControllerAdvice(controllerAdvice)
                .build();
    }

    @Test
    void shouldHandlePriceNotFoundExceptionDirectly() {
        PriceNotFoundException ex = new PriceNotFoundException();
        ProblemDetail problemDetail = controllerAdvice.handlePriceNotFoundException(ex);

        assertNotNull(problemDetail);
        assertEquals(HttpStatus.NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals("Price not found", problemDetail.getTitle());
        assertEquals("Price was not found", problemDetail.getDetail());
    }

    @Test
    void shouldHandleUnknownExceptionDirectly() {
        Exception ex = new RuntimeException("Unexpected error");
        ProblemDetail problemDetail = controllerAdvice.handleUnknownException(ex);

        assertNotNull(problemDetail);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Internal Server Error", problemDetail.getTitle());
        assertEquals("An unexpected error occurred while querying the price service", problemDetail.getDetail());
    }

    @Test
    void shouldHandleInvalidPriceCriteriaExceptionDirectly() {
        InvalidPriceCriteriaException ex = new InvalidPriceCriteriaException("Invalid price criteria");
        ProblemDetail problemDetail = controllerAdvice.handleInvalidPriceCriteriaException(ex);

        assertNotNull(problemDetail);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problemDetail.getStatus());
        assertEquals("Internal Server Error", problemDetail.getTitle());
        assertEquals("An unexpected error occurred while querying the price service", problemDetail.getDetail());
    }

    @Test
    void shouldReturnProblemDetailWithStatus404WhenPriceNotFoundExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/test/price-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Price not found"))
                .andExpect(jsonPath("$.detail").value("Price was not found"));
    }

    @Test
    void shouldReturnProblemDetailWithStatus500WhenUnknownExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/test/unknown-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred while querying the price service"));
    }

    @Test
    void shouldReturnProblemDetailWithStatus500WhenInvalidPriceCriteriaExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/test/invalid-criteria"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred while querying the price service"));
    }
}
