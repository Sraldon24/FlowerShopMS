package com.champsoft.services.sales.DataLayer.Identifiers;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentIdentifier {
    private String paymentId;
}
