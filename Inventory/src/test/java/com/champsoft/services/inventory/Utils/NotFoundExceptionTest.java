package com.champsoft.services.inventory.Utils;

import com.champsoft.services.inventory.utils.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotFoundExceptionTest {

    @Test
    void testEmptyConstructor() {
        NotFoundException exception = new NotFoundException();
        assertNull(exception.getMessage());
    }

    @Test
    void testConstructorWithMessage() {
        NotFoundException exception = new NotFoundException("Not found");
        assertEquals("Not found", exception.getMessage());
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Something went wrong");
        NotFoundException exception = new NotFoundException(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Something went wrong");
        NotFoundException exception = new NotFoundException("Not found", cause);
        assertEquals("Not found", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
