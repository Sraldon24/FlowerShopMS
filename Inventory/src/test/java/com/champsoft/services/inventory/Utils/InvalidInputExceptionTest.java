package com.champsoft.services.inventory.Utils;

import com.champsoft.services.inventory.utils.InvalidInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvalidInputExceptionTest {

    @Test
    void testConstructorWithMessage() {
        InvalidInputException exception = new InvalidInputException("Invalid input");
        assertEquals("Invalid input", exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        InvalidInputException exception = new InvalidInputException("Invalid input", cause);
        assertEquals("Invalid input", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
