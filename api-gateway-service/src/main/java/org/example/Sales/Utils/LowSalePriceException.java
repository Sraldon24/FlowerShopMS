package org.example.Sales.Utils;

public class LowSalePriceException extends RuntimeException {
    public LowSalePriceException(String message) {
        super(message);
    }
}
