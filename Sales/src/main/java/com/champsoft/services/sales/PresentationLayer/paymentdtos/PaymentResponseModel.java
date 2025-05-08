package com.champsoft.services.sales.PresentationLayer.paymentdtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseModel {
    private String paymentIdentifier; // ✅ matches JSON
    private Double amount;
    private String method;
    private String status;
    private ZonedDateTime timestamp;
    private String transactionRef;
}
