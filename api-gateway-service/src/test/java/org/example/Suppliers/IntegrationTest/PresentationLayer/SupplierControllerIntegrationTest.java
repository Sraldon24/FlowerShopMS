package org.example.Suppliers.IntegrationTest.PresentationLayer;

import org.example.Suppliers.BusinessLayer.SupplierServiceImpl;
import org.example.Suppliers.DomainClientLayer.SupplierServiceClient;
import org.example.Suppliers.PresentationLayer.PhoneNumberDTO;
import org.example.Suppliers.PresentationLayer.SupplierController;
import org.example.Suppliers.PresentationLayer.SupplierRequestModel;
import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = SupplierController.class)
public class SupplierControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SupplierServiceImpl supplierService;

    @MockBean
    private SupplierServiceClient supplierServiceClient;

    @Test
    public void testGetAllSuppliers_returnsList() {
        SupplierResponseModel mockResponse = new SupplierResponseModel();
        mockResponse.setSupplierId("sup-123");
        mockResponse.setCompanyName("Company ABC");

        when(supplierService.getSuppliers()).thenReturn(List.of(mockResponse));

        webTestClient.get()
                .uri("/api/v1/suppliers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].supplierId").isEqualTo("sup-123")
                .jsonPath("$[0].companyName").isEqualTo("Company ABC");
    }

    @Test
    public void testGetSupplierById_returnsSupplier() {
        SupplierResponseModel mockResponse = new SupplierResponseModel();
        mockResponse.setSupplierId("sup-456");
        mockResponse.setCompanyName("Company XYZ");

        when(supplierService.getSupplierBySupplierId("sup-456")).thenReturn(mockResponse);

        webTestClient.get()
                .uri("/api/v1/suppliers/sup-456")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.supplierId").isEqualTo("sup-456")
                .jsonPath("$.companyName").isEqualTo("Company XYZ");
    }

    @Test
    public void testAddSupplier_returnsCreated() {
        SupplierRequestModel request = new SupplierRequestModel();
        request.setCompanyName("New Company");
        request.setPassword1("secret");
        request.setPassword2("secret");
        request.setPhoneNumbers(List.of(new PhoneNumberDTO("MOBILE", "514-123-0000")));

        SupplierResponseModel response = new SupplierResponseModel();
        response.setSupplierId("sup-new");
        response.setCompanyName("New Company");

        when(supplierServiceClient.addSupplier(any())).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.supplierId").isEqualTo("sup-new")
                .jsonPath("$.companyName").isEqualTo("New Company");
    }

    @Test
    public void testUpdateSupplier_returnsUpdated() {
        SupplierRequestModel request = new SupplierRequestModel();
        request.setCompanyName("Mohamed");
        request.setPassword1("secret");
        request.setPassword2("secret");

        SupplierResponseModel response = new SupplierResponseModel();
        response.setSupplierId("sup-5678");
        response.setCompanyName("Mohamed");

        when(supplierServiceClient.updateSupplier("sup-5678", request)).thenReturn(response);
        when(supplierService.updateSupplier("sup-5678", request)).thenReturn(response);

        webTestClient.put()
                .uri("/api/v1/suppliers/sup-5678")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(result -> {
                    byte[] rawBody = result.getResponseBody();
                    String bodyAsString = (rawBody == null) ? "null" : new String(rawBody);
                    System.out.println("🔍 RESPONSE BODY: " + bodyAsString);
                });
    }




    @Test
    public void testDeleteSupplier_returnsSuccessMessage() {
        when(supplierService.deleteSupplierBySupplierId("sup-del")).thenReturn("Supplier deleted successfully.");

        webTestClient.delete()
                .uri("/api/v1/suppliers/sup-del")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Supplier deleted successfully.");
    }
}
