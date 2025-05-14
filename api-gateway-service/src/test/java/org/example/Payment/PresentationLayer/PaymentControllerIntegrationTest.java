package org.example.Payment.PresentationLayer;

import org.example.Payment.BusinessLayer.PaymentServiceImpl;
import org.example.Payment.DomainClientLayer.PaymentServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PaymentController.class)
public class PaymentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PaymentServiceImpl paymentService;

    @MockBean
    private PaymentServiceClient paymentServiceClient;

    @Test
    public void testGetPayments_returnsList() {
        PaymentResponseModel payment = new PaymentResponseModel("pay-001", 100.0, "CARD", "COMPLETED",
                ZonedDateTime.now(), "txn-123");

        when(paymentService.getPayments()).thenReturn(List.of(payment));

        webTestClient.get()
                .uri("/api/v1/payments")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].paymentIdentifier").isEqualTo("pay-001")
                .jsonPath("$[0].method").isEqualTo("CARD");
    }

    @Test
    public void testGetPaymentById_returnsPayment() {
        PaymentResponseModel payment = new PaymentResponseModel("pay-002", 200.0, "CASH", "PENDING",
                ZonedDateTime.now(), "txn-456");

        when(paymentService.getPaymentByPaymentId("pay-002")).thenReturn(payment);

        webTestClient.get()
                .uri("/api/v1/payments/pay-002")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paymentIdentifier").isEqualTo("pay-002")
                .jsonPath("$.status").isEqualTo("PENDING");
    }

    @Test
    public void testAddPayment_returnsCreated() {
        ZonedDateTime now = ZonedDateTime.now();
        PaymentRequestModel request = new PaymentRequestModel(150.0, "CREDIT", "PROCESSING",
                now, "txn-789");

        PaymentResponseModel response = new PaymentResponseModel("pay-003", 150.0, "CREDIT", "PROCESSING",
                now, "txn-789");

        // ✅ Mock the correct service: the one injected in PaymentController
        when(paymentService.addPayment(any(PaymentRequestModel.class))).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.paymentIdentifier").isEqualTo("pay-003")
                .jsonPath("$.method").isEqualTo("CREDIT");
    }

    @Test
    public void testUpdatePayment_returnsUpdated() {
        ZonedDateTime fixedTime = ZonedDateTime.parse("2025-04-20T14:30:00Z");

        PaymentRequestModel request = new PaymentRequestModel(
                77.25, "PAYPAL", "PENDING", fixedTime, "TXN456DEFMohamed"
        );

        PaymentResponseModel response = new PaymentResponseModel(
                "PAY-DEF456", 77.25, "PAYPAL", "PENDING", fixedTime, "TXN456DEFMohamed"
        );

        // Use argument matchers so the mock works properly
        when(paymentService.updatePayment(anyString(), any(PaymentRequestModel.class))).thenReturn(response);

        webTestClient.put()
                .uri("/api/v1/payments/PAY-DEF456")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.paymentIdentifier").isEqualTo("PAY-DEF456")
                .jsonPath("$.method").isEqualTo("PAYPAL")
                .jsonPath("$.amount").isEqualTo(77.25)
                .jsonPath("$.status").isEqualTo("PENDING")
                .jsonPath("$.transactionRef").isEqualTo("TXN456DEFMohamed")
                .jsonPath("$.timestamp").isEqualTo("2025-04-20T14:30:00Z");
    }

    @Test
    public void testDeletePayment_returnsSuccessMessage() {
        when(paymentService.deletePaymentByPaymentId("pay-del")).thenReturn("Payment deleted successfully.");

        webTestClient.delete()
                .uri("/api/v1/payments/pay-del")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Payment deleted successfully.");
    }
}