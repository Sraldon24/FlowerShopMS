package com.champsoft.services.payment.MapperLayer;


import com.champsoft.services.payment.DataLayer.Payment;
import com.champsoft.services.payment.DataLayer.PaymentMethod;
import com.champsoft.services.payment.DataLayer.PaymentStatus;
import com.champsoft.services.payment.PresentationLayer.PaymentRequestModel;
import com.champsoft.services.payment.PresentationLayer.PaymentResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MapperTests {
    private final PaymentResponseMapper mapper = Mappers.getMapper(PaymentResponseMapper.class);
    private final PaymentRequestMapper requestMapper = Mappers.getMapper(PaymentRequestMapper.class);

    @Test
    void testEntityToResponseModel() {
        Payment payment = new Payment();
        payment.setPaymentIdentifier("pay-101");
        payment.setAmount(200.50);
        payment.setMethod(PaymentMethod.CREDIT_CARD);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTimestamp(ZonedDateTime.now());
        payment.setTransactionRef("txn-ref-2025");

        PaymentResponseModel response = mapper.entityToResponseModel(payment);

        assertEquals(payment.getPaymentIdentifier(), response.getPaymentIdentifier());
        assertEquals(payment.getAmount(), response.getAmount());
        assertEquals(payment.getMethod().toString(), response.getMethod());
        assertEquals(payment.getStatus().toString(), response.getStatus());
        assertEquals(payment.getTimestamp(), response.getTimestamp());
        assertEquals(payment.getTransactionRef(), response.getTransactionRef());
    }

    @Test
    void testEntityListToResponseModelList() {
        Payment payment1 = new Payment();
        payment1.setPaymentIdentifier("pay-001");
        payment1.setAmount(100.00);
        payment1.setMethod(PaymentMethod.DEBIT_CARD);  // 👈 you're using DEBIT_CARD
        payment1.setStatus(PaymentStatus.PENDING);
        payment1.setTimestamp(ZonedDateTime.now());
        payment1.setTransactionRef("tx-ref-1");

        Payment payment2 = new Payment();
        payment2.setPaymentIdentifier("pay-002");
        payment2.setAmount(250.75);
        payment2.setMethod(PaymentMethod.CREDIT_CARD);  // 👈 you're using CREDIT_CARD
        payment2.setStatus(PaymentStatus.COMPLETED);
        payment2.setTimestamp(ZonedDateTime.now());
        payment2.setTransactionRef("tx-ref-2");

        List<Payment> paymentList = List.of(payment1, payment2);
        List<PaymentResponseModel> responseList = mapper.entityListToResponseModelList(paymentList);

        assertEquals(2, responseList.size());

        assertEquals("pay-001", responseList.get(0).getPaymentIdentifier());
        assertEquals("DEBIT_CARD", responseList.get(0).getMethod()); // ✅ match expectation

        assertEquals("pay-002", responseList.get(1).getPaymentIdentifier());
        assertEquals("CREDIT_CARD", responseList.get(1).getMethod()); // ✅ match expectation
    }

    @Test
    void testRequestModelToEntity() {
        PaymentRequestModel request = new PaymentRequestModel();
        request.setAmount(150.50);
        request.setMethod("DEBIT_CARD");
        request.setStatus("PENDING");
        request.setTimestamp(ZonedDateTime.now());
        request.setTransactionRef("tx-abc-123");

        Payment entity = requestMapper.requestModelToEntity(request);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getPaymentIdentifier());

        assertEquals(150.50, entity.getAmount());
        assertEquals(PaymentMethod.DEBIT_CARD, entity.getMethod());
        assertEquals(PaymentStatus.PENDING, entity.getStatus());
        assertEquals(request.getTimestamp(), entity.getTimestamp());
        assertEquals("tx-abc-123", entity.getTransactionRef());
    }
}
