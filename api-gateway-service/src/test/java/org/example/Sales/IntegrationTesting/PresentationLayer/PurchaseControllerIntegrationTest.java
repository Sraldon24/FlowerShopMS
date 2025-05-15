package org.example.Sales.IntegrationTesting.PresentationLayer;


import org.example.Sales.BusinessLayer.PurchaseServiceImpl;
import org.example.Sales.DataLayer.FinancingAgreementDetails;
import org.example.Sales.DataLayer.PurchaseStatus;
import org.example.Sales.PresentationLayer.PurchaseController;
import org.example.Sales.PresentationLayer.PurchaseRequestModel;
import org.example.Sales.PresentationLayer.PurchaseResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PurchaseController.class)
public class PurchaseControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PurchaseServiceImpl purchaseService;

    private final PurchaseResponseModel sampleResponse = new PurchaseResponseModel(
            "po-001", "inv-1001", null,
            "flw-1001", null,
            "sup-2001", null,
            "emp-3001", "pay-4001", null,
            new BigDecimal("199.99"), "CAD",
            LocalDate.of(2025, 5, 15), PurchaseStatus.COMPLETED,
            new FinancingAgreementDetails(12, 5.5, 1000.0)
    );

    @Test
    public void testGetAllPurchases_returnsList() {
        when(purchaseService.getAllPurchases()).thenReturn(List.of(sampleResponse));

        webTestClient.get()
                .uri("/api/v1/purchases")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].purchaseOrderId").isEqualTo("po-001")
                .jsonPath("$[0].currency").isEqualTo("CAD");
    }

    @Test
    public void testGetPurchaseById_returnsPurchase() {
        when(purchaseService.getPurchaseById("po-001")).thenReturn(sampleResponse);

        webTestClient.get()
                .uri("/api/v1/purchases/po-001")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.purchaseOrderId").isEqualTo("po-001")
                .jsonPath("$.supplierId").isEqualTo("sup-2001");
    }

    @Test
    public void testCreatePurchase_returnsCreated() {
        PurchaseRequestModel request = new PurchaseRequestModel(
                "po-002", "inv-2001", "flw-2001", "sup-2002", "emp-3002", "pay-4002",
                299.95, "CAD", "USD", LocalDate.of(2025, 6, 1),
                PurchaseStatus.PENDING, new FinancingAgreementDetails(24, 4.0, 500.0)
        );

        PurchaseResponseModel response = new PurchaseResponseModel(
                "po-002", "inv-2001", null, "flw-2001", null, "sup-2002", null,
                "emp-3002", "pay-4002", null, new BigDecimal("299.95"), "CAD",
                LocalDate.of(2025, 6, 1), PurchaseStatus.PENDING,
                new FinancingAgreementDetails(24, 4.0, 500.0)
        );

        when(purchaseService.createPurchase(any(PurchaseRequestModel.class))).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.purchaseOrderId").isEqualTo("po-002")
                .jsonPath("$.paymentId").isEqualTo("pay-4002");
    }

    @Test
    public void testUpdatePurchase_returnsUpdated() {
        PurchaseRequestModel request = new PurchaseRequestModel(
                "po-001", "inv-1001", "flw-1001", "sup-2001", "emp-3001", "pay-4001",
                219.99, "CAD", "USD", LocalDate.of(2025, 5, 20),
                PurchaseStatus.COMPLETED, new FinancingAgreementDetails(12, 5.5, 1000.0)
        );

        when(purchaseService.updatePurchase(anyString(), any(PurchaseRequestModel.class)))
                .thenReturn(sampleResponse);

        webTestClient.put()
                .uri("/api/v1/purchases/po-001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.purchaseOrderId").isEqualTo("po-001")
                .jsonPath("$.salePrice").isEqualTo(199.99);
    }

    @Test
    public void testDeletePurchase_returnsOk() {
        webTestClient.delete()
                .uri("/api/v1/purchases/po-del")
                .exchange()
                .expectStatus().isOk();
    }
}
