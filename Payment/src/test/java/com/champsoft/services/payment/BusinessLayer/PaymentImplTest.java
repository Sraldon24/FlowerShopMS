package com.champsoft.services.payment.BusinessLayer;


import com.champsoft.services.payment.BusniessLayer.PaymentServiceImpl;
import com.champsoft.services.payment.DataLayer.Payment;
import com.champsoft.services.payment.DataLayer.PaymentRepository;
import com.champsoft.services.payment.MapperLayer.PaymentRequestMapper;
import com.champsoft.services.payment.MapperLayer.PaymentResponseMapper;
import com.champsoft.services.payment.PresentationLayer.PaymentRequestModel;
import com.champsoft.services.payment.PresentationLayer.PaymentResponseModel;
import com.champsoft.services.payment.utils.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentImplTest {


    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentRequestMapper paymentRequestMapper;

    @Mock
    private PaymentResponseMapper paymentResponseMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;
    private PaymentRequestModel requestModel;
    private PaymentResponseModel responseModel;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1);
        payment.setPaymentIdentifier("pay-123");
        payment.setAmount(100.0);

        requestModel = new PaymentRequestModel(100.0, "CARD", "SUCCESS", ZonedDateTime.now(), "tx-ref-1");

        responseModel = new PaymentResponseModel("pay-123", 100.0, "CARD", "SUCCESS", ZonedDateTime.now(), "tx-ref-1");
    }

    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));
        when(paymentResponseMapper.entityListToResponseModelList(anyList())).thenReturn(List.of(responseModel));

        List<PaymentResponseModel> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testGetPaymentById_found() {
        when(paymentRepository.findByPaymentIdentifier("pay-123")).thenReturn(Optional.of(payment));
        when(paymentResponseMapper.entityToResponseModel(payment)).thenReturn(responseModel);

        PaymentResponseModel result = paymentService.getPaymentById("pay-123");

        assertEquals("pay-123", result.getPaymentIdentifier());
        verify(paymentRepository, times(1)).findByPaymentIdentifier("pay-123");
    }

    @Test
    void testGetPaymentById_notFound() {
        when(paymentRepository.findByPaymentIdentifier("not-found")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.getPaymentById("not-found"));
        verify(paymentRepository, times(1)).findByPaymentIdentifier("not-found");
    }

    @Test
    void testAddPayment() {
        when(paymentRequestMapper.requestModelToEntity(requestModel)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentResponseMapper.entityToResponseModel(payment)).thenReturn(responseModel);

        PaymentResponseModel result = paymentService.addPayment(requestModel);

        assertEquals("pay-123", result.getPaymentIdentifier());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testUpdatePayment_found() {
        Payment updatedPayment = new Payment();
        updatedPayment.setId(1);
        updatedPayment.setPaymentIdentifier("pay-123");

        when(paymentRepository.findByPaymentIdentifier("pay-123")).thenReturn(Optional.of(payment));
        when(paymentRequestMapper.requestModelToEntity(requestModel)).thenReturn(updatedPayment);
        when(paymentRepository.save(updatedPayment)).thenReturn(updatedPayment);
        when(paymentResponseMapper.entityToResponseModel(updatedPayment)).thenReturn(responseModel);

        PaymentResponseModel result = paymentService.updatePayment("pay-123", requestModel);

        assertEquals("pay-123", result.getPaymentIdentifier());
        verify(paymentRepository, times(1)).findByPaymentIdentifier("pay-123");
        verify(paymentRepository, times(1)).save(updatedPayment);
    }

    @Test
    void testUpdatePayment_notFound() {
        when(paymentRepository.findByPaymentIdentifier("invalid")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.updatePayment("invalid", requestModel));
        verify(paymentRepository, times(1)).findByPaymentIdentifier("invalid");
    }

    @Test
    void testDeletePayment_found() {
        when(paymentRepository.findByPaymentIdentifier("pay-123")).thenReturn(Optional.of(payment));

        String result = paymentService.deletePayment("pay-123");

        assertEquals("Payment with ID pay-123 deleted successfully.", result);
        verify(paymentRepository, times(1)).delete(payment);
    }

    @Test
    void testDeletePayment_notFound() {
        when(paymentRepository.findByPaymentIdentifier("bad-id")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.deletePayment("bad-id"));
        verify(paymentRepository, times(1)).findByPaymentIdentifier("bad-id");
    }
}
