
package com.champsoft.services.payment.PresentationLayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponseModel {
    private String paymentIdentifier;
    private Double amount;
    private String method;
    private String status;
    private ZonedDateTime timestamp;
    private String transactionRef;
}
