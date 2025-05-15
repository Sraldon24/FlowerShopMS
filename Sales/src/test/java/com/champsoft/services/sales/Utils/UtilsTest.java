package com.champsoft.services.sales.Utils;

import com.champsoft.services.sales.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilsTest {

    // --- GlobalExceptionHandler Tests ---

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundException_returnsProperResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/path");

        ResponseEntity<Object> response = handler.handleNotFoundException(
                new NotFoundException("Not found"), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertBodyContainsMessage(response, "Not found");
    }

    @Test
    void handleLowSalePriceException_returnsProperResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/low");

        ResponseEntity<Object> response = handler.handleLowSalePriceException(
                new LowSalePriceException("Price too low"), request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertBodyContainsMessage(response, "Price too low");
    }

    @Test
    void handleGenericException_returnsProperResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test/error");

        ResponseEntity<Object> response = handler.handleAllExceptions(
                new RuntimeException("Something failed"), request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertBodyContainsMessage(response, "Unexpected server error: Something failed");
    }

    private void assertBodyContainsMessage(ResponseEntity<Object> response, String expectedMessage) {
        assertNotNull(response.getBody());
        assertInstanceOf(Map.class, response.getBody());

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(expectedMessage, body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertNotNull(body.get("path"));
    }

    // --- NotFoundException Tests ---

    @Test
    void notFoundException_defaultConstructor() {
        NotFoundException ex = new NotFoundException();
        assertNull(ex.getMessage());
    }

    @Test
    void notFoundException_withMessage() {
        NotFoundException ex = new NotFoundException("Not here");
        assertEquals("Not here", ex.getMessage());
    }

    @Test
    void notFoundException_withCause() {
        Throwable cause = new RuntimeException("Cause");
        NotFoundException ex = new NotFoundException(cause);
        assertEquals(cause, ex.getCause());
    }

    @Test
    void notFoundException_withMessageAndCause() {
        Throwable cause = new RuntimeException("Cause");
        NotFoundException ex = new NotFoundException("Msg", cause);
        assertEquals("Msg", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    // --- InvalidInputException Tests ---

    @Test
    void invalidInputException_withMessage() {
        InvalidInputException ex = new InvalidInputException("Invalid!");
        assertEquals("Invalid!", ex.getMessage());
    }

    @Test
    void invalidInputException_withMessageAndCause() {
        Throwable cause = new RuntimeException("Oops");
        InvalidInputException ex = new InvalidInputException("Bad input", cause);
        assertEquals("Bad input", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    // --- LowSalePriceException (Already 100%) ---

    @Test
    void lowSalePriceException_basicConstructor() {
        LowSalePriceException ex = new LowSalePriceException("Too low");
        assertEquals("Too low", ex.getMessage());
    }

    // --- HttpErrorInfo ---

    @Test
    void httpErrorInfo_constructor_setsAllFields() {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpErrorInfo errorInfo = new HttpErrorInfo(status, "/path", "Error occurred");

        assertEquals(status, errorInfo.getHttpStatus());
        assertEquals("/path", errorInfo.getPath());
        assertEquals("Error occurred", errorInfo.getMessage());
        assertNotNull(errorInfo.getTimestamp());
    }

    // --- Currency Enum ---

    @Test
    void currency_enumValues() {
        Currency[] values = Currency.values();
        assertEquals(4, values.length);
        assertEquals(Currency.CAD, Currency.valueOf("CAD"));
        assertEquals(Currency.USD, Currency.valueOf("USD"));
        assertEquals(Currency.SAR, Currency.valueOf("SAR"));
        assertEquals(Currency.EUR, Currency.valueOf("EUR"));
    }
}
