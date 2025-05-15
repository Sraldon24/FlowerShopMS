package org.example.Payment.PresentationLayer;


import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentHateoasWrapperTest {

    @Test
    void testPaymentResponseModel_basicMethods() {
        // Initialize and set fields
        PaymentResponseModel model = new PaymentResponseModel();
        model.setPaymentIdentifier("p1");
        model.setAmount(100.0);
        model.setMethod("CreditCard");
        model.setStatus("Completed");
        model.setTimestamp(ZonedDateTime.parse("2023-01-01T10:00:00Z"));
        model.setTransactionRef("txn123");

        // Basic getters
        assertEquals("p1", model.getPaymentIdentifier());
        assertEquals(100.0, model.getAmount());
        assertEquals("CreditCard", model.getMethod());
        assertEquals("Completed", model.getStatus());
        assertEquals(ZonedDateTime.parse("2023-01-01T10:00:00Z"), model.getTimestamp());
        assertEquals("txn123", model.getTransactionRef());

        // equals and hashCode reflexivity
        assertEquals(model, model);
        assertEquals(model.hashCode(), model.hashCode());

        // Equals with identical values
        PaymentResponseModel same = new PaymentResponseModel();
        same.setPaymentIdentifier("p1");
        same.setAmount(100.0);
        same.setMethod("CreditCard");
        same.setStatus("Completed");
        same.setTimestamp(ZonedDateTime.parse("2023-01-01T10:00:00Z"));
        same.setTransactionRef("txn123");

        assertEquals(model, same);
        assertEquals(model.hashCode(), same.hashCode());

        PaymentResponseModel diff = new PaymentResponseModel();
        diff.setPaymentIdentifier("DIFFERENT_ID");
        diff.setAmount(999.99);
        diff.setMethod("WireTransfer");
        diff.setStatus("Failed");
        diff.setTimestamp(ZonedDateTime.parse("2024-01-01T00:00:00Z"));
        diff.setTransactionRef("DIFF_TXN");

        // Add a HATEOAS link to make 'diff' truly different
        diff.add(Link.of("http://localhost/api/payments/DIFFERENT_ID"));

        // Ensure it's truly different
        assertNotEquals(model, diff);

        // Not equals with null and other object
        assertNotEquals(model, null);
        assertNotEquals(model, new Object());

        // toString should not be null or empty
        assertNotNull(model.toString());
        assertFalse(model.toString().isEmpty());
    }



    @Test
    void testPaymentHateoasWrapper_Embedded_basicMethods() {
        PaymentResponseModel payment = new PaymentResponseModel();
        List<PaymentResponseModel> list = List.of(payment);

        PaymentHateoasWrapper.Embedded embedded = new PaymentHateoasWrapper.Embedded(list);

        assertEquals(list, embedded.getPaymentList());

        embedded.setPaymentList(Collections.emptyList());
        assertEquals(Collections.emptyList(), embedded.getPaymentList());

        // Equals and hashCode
        PaymentHateoasWrapper.Embedded sameEmbedded = new PaymentHateoasWrapper.Embedded(Collections.emptyList());
        assertEquals(embedded, sameEmbedded);
        assertEquals(embedded.hashCode(), sameEmbedded.hashCode());

        PaymentHateoasWrapper.Embedded diffEmbedded = new PaymentHateoasWrapper.Embedded(null);
        assertNotEquals(embedded, diffEmbedded);
        assertNotEquals(embedded, null);
        assertNotEquals(embedded, new Object());

        // canEqual
        assertTrue(embedded.canEqual(sameEmbedded));
        assertFalse(embedded.canEqual(new Object()));

        assertNotNull(embedded.toString());

        // No-arg constructor
        PaymentHateoasWrapper.Embedded noArg = new PaymentHateoasWrapper.Embedded();
        assertNull(noArg.getPaymentList());
    }

    @Test
    void testPaymentHateoasWrapper_basicMethods() {
        PaymentResponseModel payment = new PaymentResponseModel();
        List<PaymentResponseModel> list = List.of(payment);
        PaymentHateoasWrapper.Embedded embedded = new PaymentHateoasWrapper.Embedded(list);

        PaymentHateoasWrapper wrapper = new PaymentHateoasWrapper(embedded);

        assertEquals(embedded, wrapper.getEmbedded());

        wrapper.setEmbedded(null);
        assertNull(wrapper.getEmbedded());

        // Equals and hashCode
        PaymentHateoasWrapper wrapper1 = new PaymentHateoasWrapper(null);
        PaymentHateoasWrapper wrapper2 = new PaymentHateoasWrapper(null);

        assertEquals(wrapper1, wrapper2);
        assertEquals(wrapper1.hashCode(), wrapper2.hashCode());

        PaymentHateoasWrapper wrapper3 = new PaymentHateoasWrapper(embedded);
        assertNotEquals(wrapper1, wrapper3);

        // canEqual
        assertTrue(wrapper1.canEqual(wrapper2));
        assertFalse(wrapper1.canEqual(new Object()));

        assertNotNull(wrapper.toString());

        // No-arg constructor
        PaymentHateoasWrapper emptyWrapper = new PaymentHateoasWrapper();
        assertNull(emptyWrapper.getEmbedded());
    }
}
