package com.champsoft.services.payment.PresentationLayer;

import com.champsoft.services.payment.BusniessLayer.PaymentService;
import com.champsoft.services.payment.DataLayer.PaymentMethod;
import com.champsoft.services.payment.DataLayer.PaymentStatus;
import com.champsoft.services.payment.utils.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTests {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentRequestModel sampleRequest;
    private PaymentResponseModel sampleResponse;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sampleRequest = new PaymentRequestModel();
        sampleRequest.setAmount(100.0);
        sampleRequest.setMethod("CREDIT_CARD");
        sampleRequest.setStatus("COMPLETED");
        sampleRequest.setTimestamp(ZonedDateTime.now());
        sampleRequest.setTransactionRef("txn-123");

        sampleResponse = new PaymentResponseModel();
        sampleResponse.setPaymentIdentifier("pay-001");
        sampleResponse.setAmount(100.0);
        sampleResponse.setMethod("CREDIT_CARD");
        sampleResponse.setStatus("COMPLETED");
        sampleResponse.setTimestamp(sampleRequest.getTimestamp());
        sampleResponse.setTransactionRef("txn-123");
    }

    @Test
    void testGetAllPayments() {
        when(paymentService.getAllPayments()).thenReturn(List.of(sampleResponse));

        ResponseEntity<CollectionModel<PaymentResponseModel>> response = paymentController.getAllPayments();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getContent().size());
        verify(paymentService).getAllPayments();
    }


    @Test
    void testGetPaymentById() {
        when(paymentService.getPaymentById("pay-001")).thenReturn(sampleResponse);

        ResponseEntity<PaymentResponseModel> response = paymentController.getPaymentById("pay-001");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("pay-001", response.getBody().getPaymentIdentifier());
        verify(paymentService).getPaymentById("pay-001");
    }
    @Test
    void testGetPaymentById_NotFound() {
        when(paymentService.getPaymentById("invalid-id"))
                .thenThrow(new NotFoundException("Payment not found"));

        assertThrows(NotFoundException.class, () -> paymentController.getPaymentById("invalid-id"));
        verify(paymentService, times(1)).getPaymentById("invalid-id");
    }
    @Test
    void testAddPayment() {
        when(paymentService.addPayment(sampleRequest)).thenReturn(sampleResponse);

        ResponseEntity<PaymentResponseModel> response = paymentController.addPayment(sampleRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("pay-001", response.getBody().getPaymentIdentifier());
        verify(paymentService).addPayment(sampleRequest);
    }

    @Test
    void testUpdatePayment() {
        when(paymentService.updatePayment("pay-001", sampleRequest)).thenReturn(sampleResponse);

        ResponseEntity<PaymentResponseModel> response = paymentController.updatePayment("pay-001", sampleRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("pay-001", response.getBody().getPaymentIdentifier());
        verify(paymentService).updatePayment("pay-001", sampleRequest);
    }

    @Test
    void testDeletePayment() {
        when(paymentService.deletePayment("pay-001")).thenReturn("Deleted");

        ResponseEntity<String> response = paymentController.deletePayment("pay-001");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted", response.getBody());
        verify(paymentService).deletePayment("pay-001");
    }

}
