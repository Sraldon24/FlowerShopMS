
package com.champsoft.services.payment.BusniessLayer;

import com.champsoft.services.payment.DataLayer.Payment;
import com.champsoft.services.payment.DataLayer.PaymentRepository;
import com.champsoft.services.payment.MapperLayer.PaymentRequestMapper;
import com.champsoft.services.payment.MapperLayer.PaymentResponseMapper;
import com.champsoft.services.payment.PresentationLayer.PaymentRequestModel;
import com.champsoft.services.payment.PresentationLayer.PaymentResponseModel;
import com.champsoft.services.payment.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentResponseMapper paymentResponseMapper;
    private final PaymentRequestMapper paymentRequestMapper;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentResponseMapper paymentResponseMapper,
                              PaymentRequestMapper paymentRequestMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentResponseMapper = paymentResponseMapper;
        this.paymentRequestMapper = paymentRequestMapper;
    }

    @Override
    public List<PaymentResponseModel> getAllPayments() {
        return paymentResponseMapper.entityListToResponseModelList(paymentRepository.findAll());
    }

    @Override
    public PaymentResponseModel getPaymentById(String paymentIdentifier) {
        log.info("Fetching payment with ID: {}", paymentIdentifier);
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentIdentifier(paymentIdentifier);
        if (paymentOpt.isEmpty()) {
            log.error("Payment with ID {} not found.", paymentIdentifier);
            throw new NotFoundException("Payment with ID " + paymentIdentifier + " not found.");
        }
        return paymentResponseMapper.entityToResponseModel(paymentOpt.get());
    }

    @Override
    public PaymentResponseModel addPayment(PaymentRequestModel paymentRequest) {
        Payment payment = paymentRequestMapper.requestModelToEntity(paymentRequest);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentResponseMapper.entityToResponseModel(savedPayment);
    }

    @Override
    public PaymentResponseModel updatePayment(String paymentIdentifier, PaymentRequestModel updatedPayment) {
        Optional<Payment> existingOpt = paymentRepository.findByPaymentIdentifier(paymentIdentifier);
        if (existingOpt.isEmpty()) {
            log.error("Payment with ID {} not found.", paymentIdentifier);
            throw new NotFoundException("Payment with ID " + paymentIdentifier + " not found.");
        }

        Payment existing = existingOpt.get();
        Payment payment = paymentRequestMapper.requestModelToEntity(updatedPayment);
        payment.setId(existing.getId());
        payment.setPaymentIdentifier(paymentIdentifier);

        Payment saved = paymentRepository.save(payment);
        return paymentResponseMapper.entityToResponseModel(saved);
    }

    @Override
    public String deletePayment(String paymentIdentifier) {
        Optional<Payment> paymentOpt = paymentRepository.findByPaymentIdentifier(paymentIdentifier);
        if (paymentOpt.isEmpty()) {
            log.error("Payment with ID {} not found.", paymentIdentifier);
            throw new NotFoundException("Payment with ID " + paymentIdentifier + " not found.");
        }
        paymentRepository.delete(paymentOpt.get());
        return "Payment with ID " + paymentIdentifier + " deleted successfully.";
    }
}
