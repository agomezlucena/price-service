package io.github.agomezlucena.priceservice.infrastructure;

import io.github.agomezlucena.priceservice.domain.PriceNotFoundException;
import io.github.agomezlucena.priceservice.domain.criteria.InvalidPriceCriteriaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A controller advice that provides centralized exception handling for the application.
 * It extends {@code ResponseEntityExceptionHandler} to customize the default behavior
 * of exception responses and handles exceptions related to price retrieval.
 *
 * The primary purpose of this class is to handle the {@code PriceNotFoundException}
 * and return a structured error response using the {@code ProblemDetail} format.
 *
 * This ensures that when a requested price is not found, the client receives
 * a meaningful error message with relevant HTTP status code.
 */
@RestControllerAdvice
public class PriceControllerAdvice extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PriceControllerAdvice.class);
    /**
     * Handles the {@code PriceNotFoundException} by returning a structured error response
     * in the form of a {@link ProblemDetail} object. The response includes the HTTP status
     * code {@code 404 Not Found} and a descriptive error message indicating that the price
     * was not found.
     *
     * @param ex the exception object of type {@code PriceNotFoundException} that was thrown
     *           when the requested price information could not be located
     * @return a {@link ProblemDetail} object containing error details with a title,
     *         message, and the associated HTTP status code
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PriceNotFoundException.class)
    public ProblemDetail handlePriceNotFoundException(PriceNotFoundException ex) {
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Price not found");
        problem.setDetail("Price was not found");
        return problem;
    }

    /**
     * Handles the {@code InvalidPriceCriteriaException} by returning a structured error response
     * in the form of a {@link ProblemDetail} object. The response includes the HTTP status code
     * {@code 500 Internal Server Error} and a descriptive error message indicating that there was
     * an issue with the price criteria, preventing proper query execution.
     *
     * @param ex the exception object of type {@code InvalidPriceCriteriaException} that was thrown when
     *           the price query criteria was invalid or malformed
     * @return a {@link ProblemDetail} object containing error details with a title, message,
     *         and the associated HTTP status code
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidPriceCriteriaException.class)
    public ProblemDetail handleInvalidPriceCriteriaException(InvalidPriceCriteriaException ex) {
        LOG.debug("There was an error processing the query",ex);
        return createGenericError();
    }

    /**
     * Handles unexpected and unknown exceptions by returning a structured error response
     * in the form of a {@link ProblemDetail} object. The response includes the HTTP status
     * code {@code 500 Internal Server Error} and a descriptive error message indicating
     * that an unexpected error occurred.
     *
     * @param ex the unexpected exception that was thrown during request processing
     * @return a {@link ProblemDetail} object containing error details with a title,
     *         message, and the associated HTTP status code
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnknownException(Exception ex) {
        LOG.error("there was an unknown exception",ex);
        return createGenericError();
    }

    private ProblemDetail createGenericError(){
        var problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal Server Error");
        problem.setDetail("An unexpected error occurred while querying the price service");
        return problem;
    }
}
