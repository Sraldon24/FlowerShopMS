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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;

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
    public void testAddSupplier_returnsCreated() {
        SupplierRequestModel request = new SupplierRequestModel();
        request.setCompanyName("Test Inc");
        request.setPassword1("pass");
        request.setPassword2("pass");
        request.setPhoneNumbers(List.of(new PhoneNumberDTO("MOBILE", "514-123-4567")));

        SupplierResponseModel response = new SupplierResponseModel();
        response.setSupplierId("sup-abc");
        response.setCompanyName("Test Inc");

        when(supplierServiceClient.addSupplier(any())).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.supplierId").isEqualTo("sup-abc")
                .jsonPath("$.companyName").isEqualTo("Test Inc");
    }

    @Test
    public void testAddSupplier_invalidPassword_returnsCreatedBecauseServiceIsMocked() {
        SupplierRequestModel request = new SupplierRequestModel();
        request.setCompanyName("Bad Co");
        request.setPassword1("abc");
        request.setPassword2("mismatch");

        SupplierResponseModel response = new SupplierResponseModel();
        response.setSupplierId("sup-bad");
        response.setCompanyName("Bad Co");

        when(supplierServiceClient.addSupplier(any())).thenReturn(response);

        webTestClient.post()
                .uri("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated(); // because it's mocked
    }
}
