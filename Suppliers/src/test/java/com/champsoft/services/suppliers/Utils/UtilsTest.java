package com.champsoft.services.suppliers.Utils;

import com.champsoft.services.suppliers.utils.InvalidInputException;
import com.champsoft.services.suppliers.utils.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {

    @Test
    void testInvalidInputException_withMessage() {
        InvalidInputException exception = new InvalidInputException("Invalid input occurred.");
        assertEquals("Invalid input occurred.", exception.getMessage());
    }

    @Test
    void testInvalidInputException_withMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        InvalidInputException exception = new InvalidInputException("Invalid input", cause);

        assertEquals("Invalid input", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testNotFoundException_withMessage() {
        NotFoundException exception = new NotFoundException("Resource not found");
        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void testNotFoundException_withMessageAndCause() {
        Throwable cause = new RuntimeException("Database failure");
        NotFoundException exception = new NotFoundException("Not found", cause);

        assertEquals("Not found", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testInvalidInputException_withNulls() {
        InvalidInputException ex = new InvalidInputException(null, null);
        assertNull(ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void testNotFoundException_withNulls() {
        NotFoundException ex = new NotFoundException(null, null);
        assertNull(ex.getMessage());
        assertNull(ex.getCause());
    }
}