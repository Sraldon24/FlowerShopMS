package org.example.Payment.BusinessLayer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Payment.DomainClientLayer.PaymentServiceClient;
import org.example.Payment.PresentationLayer.PaymentRequestModel;
import org.example.Payment.PresentationLayer.PaymentResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl {

    private final PaymentServiceClient paymentServiceClient;

    public List<PaymentResponseModel> getPayments() {
        return paymentServiceClient.getPayments();
    }

    public PaymentResponseModel getPaymentByPaymentId(String paymentId) {
        return paymentServiceClient.getPaymentById(paymentId);
    }

    public PaymentResponseModel addPayment(PaymentRequestModel newPaymentData) {
        return paymentServiceClient.addPayment(newPaymentData);
    }

    public PaymentResponseModel updatePayment(String paymentId, PaymentRequestModel newPaymentData) {
        return paymentServiceClient.updatePayment(paymentId, newPaymentData);
    }

    public String deletePaymentByPaymentId(String paymentId) {
        return paymentServiceClient.deletePayment(paymentId);
    }
}
