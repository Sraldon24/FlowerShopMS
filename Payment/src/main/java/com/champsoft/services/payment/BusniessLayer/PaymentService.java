
package com.champsoft.services.payment.BusniessLayer;

import com.champsoft.services.payment.PresentationLayer.PaymentRequestModel;
import com.champsoft.services.payment.PresentationLayer.PaymentResponseModel;

import java.util.List;

public interface PaymentService {
    List<PaymentResponseModel> getAllPayments();
    PaymentResponseModel getPaymentById(String paymentIdentifier);
    PaymentResponseModel addPayment(PaymentRequestModel paymentRequest);
    PaymentResponseModel updatePayment(String paymentIdentifier, PaymentRequestModel updatedPayment);
    String deletePayment(String paymentIdentifier);
}
