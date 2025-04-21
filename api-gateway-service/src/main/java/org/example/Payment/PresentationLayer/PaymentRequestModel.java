package org.example.Payment.PresentationLayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequestModel {
    private Double amount;
    private String method;
    private String status;
    private ZonedDateTime timestamp;
    private String transactionRef;
}
