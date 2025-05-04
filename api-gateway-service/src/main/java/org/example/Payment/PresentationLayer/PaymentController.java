package org.example.Payment.PresentationLayer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.Payment.BusinessLayer.PaymentServiceImpl;
import org.example.Payment.Utils.InvalidInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentServiceImpl paymentService;


    @GetMapping
    public ResponseEntity<List<PaymentResponseModel>> getPayments() {
        return ResponseEntity.ok(paymentService.getPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseModel> getPaymentByPaymentId(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentByPaymentId(paymentId));
    }

    @PostMapping
    public ResponseEntity<PaymentResponseModel> addPayment(@RequestBody PaymentRequestModel newPaymentData) {
        return ResponseEntity.status(201).body(paymentService.addPayment(newPaymentData));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseModel> updatePayment(@PathVariable String paymentId,
                                                              @RequestBody PaymentRequestModel newPaymentData) {

        return ResponseEntity.ok(paymentService.updatePayment(paymentId, newPaymentData));
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<String> deletePaymentByPaymentId(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.deletePaymentByPaymentId(paymentId));
    }
}
