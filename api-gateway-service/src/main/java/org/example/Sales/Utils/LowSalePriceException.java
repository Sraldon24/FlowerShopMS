package org.example.Sales.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
public class LowSalePriceException extends RuntimeException {

    public LowSalePriceException(String message) {
        super(message);
    }
}
