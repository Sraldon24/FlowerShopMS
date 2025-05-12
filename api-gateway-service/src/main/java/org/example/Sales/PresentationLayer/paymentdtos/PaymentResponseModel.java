package org.example.Sales.PresentationLayer.paymentdtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

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
