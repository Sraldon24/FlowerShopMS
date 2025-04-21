
package com.champsoft.services.payment.DataLayer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(columnNames = "paymentIdentifier"))
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String paymentIdentifier;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private ZonedDateTime timestamp;

    private String transactionRef;

    @PrePersist
    public void prePersist() {
        if (this.paymentIdentifier == null || this.paymentIdentifier.isEmpty()) {
            this.paymentIdentifier = "PAY-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
    }
}
