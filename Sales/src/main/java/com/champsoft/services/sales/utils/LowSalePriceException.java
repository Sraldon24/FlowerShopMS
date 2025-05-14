package com.champsoft.services.sales.utils;

public class LowSalePriceException extends RuntimeException {
    public LowSalePriceException(String message) {
        super(message);
    }
}
