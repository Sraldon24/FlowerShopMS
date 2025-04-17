package com.champsoft.services.suppliers.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SupplierUtilsTest {

    @Test
    void testNotFoundExceptionMessage() {
        NotFoundException exception = new NotFoundException("Supplier not found");
        assertEquals("Supplier not found", exception.getMessage());
    }
}
