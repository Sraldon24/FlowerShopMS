package com.champsoft.services.payment.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTests {
    @Test
    void testCurrencyEnumValues() {
        assertEquals(Currency.CAD, Currency.valueOf("CAD"));
        assertEquals(Currency.USD, Currency.valueOf("USD"));
        assertEquals(Currency.SAR, Currency.valueOf("SAR"));
        assertEquals(Currency.EUR, Currency.valueOf("EUR"));

        Currency[] values = Currency.values();
        assertEquals(4, values.length);
    }

    // Test HttpErrorInfo construction
    @Test
    void testHttpErrorInfoConstruction() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String path = "/payments/123";
        String message = "Payment not found";

        HttpErrorInfo errorInfo = new HttpErrorInfo(status, path, message);

        assertEquals(status, errorInfo.getHttpStatus());
        assertEquals(path, errorInfo.getPath());
        assertEquals(message, errorInfo.getMessage());
        assertNotNull(errorInfo.getTimestamp());
        assertTrue(errorInfo.getTimestamp().isBefore(ZonedDateTime.now().plusSeconds(1)));
    }

    // NotFoundException constructors
    @Test
    void testNotFoundExceptionConstructors() {
        NotFoundException ex1 = new NotFoundException();
        NotFoundException ex2 = new NotFoundException("Not found message");
        NotFoundException ex3 = new NotFoundException(new Throwable("Cause"));
        NotFoundException ex4 = new NotFoundException("Message", new Throwable("Cause"));

        assertNull(ex1.getMessage());
        assertEquals("Not found message", ex2.getMessage());
        assertEquals("java.lang.Throwable: Cause", ex3.getCause().toString());
        assertEquals("Message", ex4.getMessage());
        assertEquals("java.lang.Throwable: Cause", ex4.getCause().toString());
    }

    // InvalidInputException constructors
    @Test
    void testInvalidInputExceptionConstructors() {
        InvalidInputException ex1 = new InvalidInputException("Invalid input");
        InvalidInputException ex2 = new InvalidInputException("Invalid input", new Throwable("Input cause"));

        assertEquals("Invalid input", ex1.getMessage());
        assertEquals("Invalid input", ex2.getMessage());
        assertEquals("java.lang.Throwable: Input cause", ex2.getCause().toString());
    }
}
