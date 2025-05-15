package com.champsoft.services.payment.DataLayer;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTests {

    @Test
    void testPrePersistGeneratesPaymentIdentifier() {
        Payment payment = new Payment();
        payment.setAmount(99.99);
        payment.setMethod(PaymentMethod.CREDIT_CARD);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTimestamp(ZonedDateTime.now());
        payment.setTransactionRef("tx-test-001");

        // Simulate JPA @PrePersist lifecycle call
        payment.prePersist();

        assertNotNull(payment.getPaymentIdentifier());
        assertTrue(payment.getPaymentIdentifier().startsWith("PAY-"));
        assertEquals(10, payment.getPaymentIdentifier().length()); // e.g., PAY-XXXXXX
    }

    @Test
    void testPrePersistDoesNotOverrideExistingIdentifier() {
        Payment payment = new Payment();
        payment.setPaymentIdentifier("PAY-EXIST");
        payment.setAmount(120.50);
        payment.setMethod(PaymentMethod.DEBIT_CARD);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTimestamp(ZonedDateTime.now());
        payment.setTransactionRef("tx-test-002");

        payment.prePersist();

        assertEquals("PAY-EXIST", payment.getPaymentIdentifier());
    }
}
