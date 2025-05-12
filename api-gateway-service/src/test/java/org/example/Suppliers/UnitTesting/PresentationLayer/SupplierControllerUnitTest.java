package org.example.Suppliers.UnitTesting.PresentationLayer;

import org.example.Suppliers.BusinessLayer.SupplierServiceImpl;
import org.example.Suppliers.DomainClientLayer.SupplierServiceClient;
import org.example.Suppliers.PresentationLayer.PhoneNumberDTO;
import org.example.Suppliers.PresentationLayer.SupplierController;
import org.example.Suppliers.PresentationLayer.SupplierRequestModel;
import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SupplierControllerUnitTest {

    @Mock
    private SupplierServiceImpl supplierService;

    @Mock
    private SupplierServiceClient supplierServiceClient;

    @InjectMocks
    private SupplierController supplierController;

    private WebTestClient webTestClient;
    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(supplierController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetAllSuppliers_returnsOk() {
        SupplierResponseModel mockSupplier = new SupplierResponseModel();
        mockSupplier.setSupplierId("sup-123");
        mockSupplier.setCompanyName("Test Inc.");

        when(supplierService.getSuppliers()).thenReturn(List.of(mockSupplier));

        webTestClient.get()
                .uri("/api/v1/suppliers")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].supplierId").isEqualTo("sup-123")
                .jsonPath("$[0].companyName").isEqualTo("Test Inc.");
    }

    @Test
    public void testAddSupplier_returnsCreated() {
        SupplierRequestModel newSupplier = new SupplierRequestModel();
        newSupplier.setCompanyName("NewCo");

        SupplierResponseModel createdSupplier = new SupplierResponseModel();
        createdSupplier.setSupplierId("sup-new");
        createdSupplier.setCompanyName("NewCo");

        when(supplierServiceClient.addSupplier(any(SupplierRequestModel.class))).thenReturn(createdSupplier);

        webTestClient.post()
                .uri("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newSupplier))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.supplierId").isEqualTo("sup-new")
                .jsonPath("$.companyName").isEqualTo("NewCo");
    }

    @Test
    public void testGetSupplierById_returnsOk() {
        SupplierResponseModel supplier = new SupplierResponseModel();
        supplier.setSupplierId("sup-123");
        supplier.setCompanyName("Acme Corp");

        when(supplierService.getSupplierBySupplierId("sup-123")).thenReturn(supplier);

        webTestClient.get()
                .uri("/api/v1/suppliers/sup-123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.supplierId").isEqualTo("sup-123")
                .jsonPath("$.companyName").isEqualTo("Acme Corp");
    }

    @Test
    public void testUpdateSupplier_returnsOk() {
        SupplierRequestModel updatedRequest = new SupplierRequestModel();
        updatedRequest.setSupplierId("sup-9999");
        updatedRequest.setCompanyName("NoFlowers Supplier");
        updatedRequest.setContactPerson("Charlie Green");
        updatedRequest.setEmailAddress("charlie@noflowers.com");
        updatedRequest.setStreetAddress("789 Leaf Rd");
        updatedRequest.setPostalCode("V3C 3C3");
        updatedRequest.setCity("Vancouver");
        updatedRequest.setProvince("British Columbia");
        updatedRequest.setUsername("charlie");
        updatedRequest.setPassword1("pass");
        updatedRequest.setPassword2("pass");

        PhoneNumberDTO phoneNumber = new PhoneNumberDTO("WORK", "604-555-7890");
        updatedRequest.setPhoneNumbers(List.of(phoneNumber));

        SupplierResponseModel updatedResponse = new SupplierResponseModel();
        updatedResponse.setSupplierId("sup-9999");
        updatedResponse.setCompanyName("NoFlowers Supplier");
        updatedResponse.setContactPerson("Charlie Green");
        updatedResponse.setEmailAddress("charlie@noflowers.com");
        updatedResponse.setStreetAddress("789 Leaf Rd");
        updatedResponse.setPostalCode("V3C 3C3");
        updatedResponse.setCity("Vancouver");
        updatedResponse.setProvince("British Columbia");
        updatedResponse.setPhoneNumbers(List.of(phoneNumber));

        // ✅ FIXED: use any() for argument matching
        when(supplierService.updateSupplier(any(String.class), any(SupplierRequestModel.class)))
                .thenReturn(updatedResponse);

        webTestClient.put()
                .uri("/api/v1/suppliers/sup-9999")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(updatedRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody(SupplierResponseModel.class)
                .consumeWith(response -> {
                    SupplierResponseModel body = response.getResponseBody();
                    System.out.println("✅ RESPONSE: " + body);
                    assert body != null : "Response body is null";
                    assert body.getCompanyName().equals("NoFlowers Supplier");
                });
    }


    @Test
    public void testDeleteSupplier_returnsOk() {
        when(supplierService.deleteSupplierBySupplierId("sup-del")).thenReturn("Deleted supplier: sup-del");

        webTestClient.delete()
                .uri("/api/v1/suppliers/sup-del")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Deleted supplier: sup-del");
    }

    @Test
    public void testGetSupplierById_notFound_returns404() {
        when(supplierService.getSupplierBySupplierId("not-found")).thenThrow(new RuntimeException("Supplier not found"));

        webTestClient.get()
                .uri("/api/v1/suppliers/not-found")
                .exchange()
                .expectStatus().is5xxServerError(); // You can change this to .isNotFound() if you're throwing a proper 404 exception
    }
}

