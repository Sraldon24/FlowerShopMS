
package com.champsoft.services.payment.PresentationLayer;

import com.champsoft.services.payment.BusniessLayer.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<PaymentResponseModel>> getAllPayments() {
        log.info("Fetching all payments");
        List<PaymentResponseModel> payments = paymentService.getAllPayments();

        payments.forEach(payment -> {
            payment.add(linkTo(methodOn(PaymentController.class)
                    .getPaymentById(payment.getPaymentIdentifier())).withRel("get-by-id"));

            payment.add(linkTo(methodOn(PaymentController.class)
                    .deletePayment(payment.getPaymentIdentifier())).withRel("delete"));
        });

        return ResponseEntity.ok(
                CollectionModel.of(payments,
                        linkTo(methodOn(PaymentController.class).getAllPayments()).withSelfRel())
        );
    }

    @GetMapping("/{paymentIdentifier}")
    public ResponseEntity<PaymentResponseModel> getPaymentById(@PathVariable String paymentIdentifier) {
        log.info("Fetching payment with ID: {}", paymentIdentifier);
        return ResponseEntity.ok(paymentService.getPaymentById(paymentIdentifier));
    }

    @PostMapping()
    public ResponseEntity<PaymentResponseModel> addPayment(@RequestBody PaymentRequestModel paymentRequest) {
        log.info("Adding new payment: {}", paymentRequest);
        return ResponseEntity.ok(paymentService.addPayment(paymentRequest));
    }

    @PutMapping("/{paymentIdentifier}")
    public ResponseEntity<PaymentResponseModel> updatePayment(@PathVariable String paymentIdentifier,
                                                              @RequestBody PaymentRequestModel paymentRequest) {
        log.info("Updating payment with ID: {}", paymentIdentifier);
        return ResponseEntity.ok(paymentService.updatePayment(paymentIdentifier, paymentRequest));
    }

    @DeleteMapping("/{paymentIdentifier}")
    public ResponseEntity<String> deletePayment(@PathVariable String paymentIdentifier) {
        log.info("Deleting payment with ID: {}", paymentIdentifier);
        return ResponseEntity.ok(paymentService.deletePayment(paymentIdentifier));
    }
}
