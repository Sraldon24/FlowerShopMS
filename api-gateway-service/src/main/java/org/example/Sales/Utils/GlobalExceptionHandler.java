package org.example.Sales.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LowSalePriceException.class)
    public ResponseEntity<Map<String, Object>> handleLowSalePriceException(LowSalePriceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "path", "/api/v1/purchases", // or extract dynamically
                "error", "Bad Request",
                "message", ex.getMessage(),
                "timestamp", ZonedDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value()
        ));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidInputException(InvalidInputException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of(
                "path", "/api/v1/purchases",
                "error", "Unprocessable Entity",
                "message", ex.getMessage(),
                "timestamp", ZonedDateTime.now(),
                "status", HttpStatus.UNPROCESSABLE_ENTITY.value()
        ));
    }
}
