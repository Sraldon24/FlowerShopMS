//package org.example.Suppliers.DomainClientLayer;
//
//import org.example.Suppliers.PresentationLayer.SupplierHateoasWrapper;
//import org.example.Suppliers.PresentationLayer.SupplierRequestModel;
//import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
//import org.example.Suppliers.Utils.InvalidInputException;
//import org.example.Suppliers.Utils.NotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class SupplierServiceClientTest {
//
//    private SupplierServiceClient client;
//
//    // Mocks for WebClient and intermediate steps
//    private WebClient mockWebClient;
//    private WebClient.RequestHeadersUriSpec<?> mockRequestHeadersUriSpec;
//    private WebClient.RequestHeadersSpec<?> mockRequestHeadersSpec;
//    private WebClient.RequestBodyUriSpec mockRequestBodyUriSpec;
//    private WebClient.RequestBodySpec mockRequestBodySpec;
//    private WebClient.ResponseSpec mockResponseSpec;
//
//    @BeforeEach
//    void setup() {
//        mockWebClient = mock(WebClient.class);
//        mockRequestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
//        mockRequestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
//        mockRequestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
//        mockRequestBodySpec = mock(WebClient.RequestBodySpec.class);
//        mockResponseSpec = mock(WebClient.ResponseSpec.class);
//
//        // Create client overriding getWebClient to return mocked WebClient
//        client = new SupplierServiceClient("localhost", "8080") {
//            @Override
//            protected WebClient getWebClient() {
//                return mockWebClient;
//            }
//        };
//    }
//
//    // Helper to create sample SupplierResponseModel
//    private SupplierResponseModel createSampleSupplier() {
//        SupplierResponseModel s = new SupplierResponseModel();
//        s.setSupplierId("supplier1");
//        s.setCompanyName("Test Company");
//        return s;
//    }
//
//    // Helper to create SupplierHateoasWrapper with one supplier
//    private SupplierHateoasWrapper createSupplierWrapper() {
//        SupplierHateoasWrapper wrapper = new SupplierHateoasWrapper();
//        wrapper.setEmbedded(new SupplierHateoasWrapper.Embedded(List.of(createSampleSupplier())));
//        return wrapper;
//    }
//
//    @Test
//    void getSuppliers_shouldReturnList() {
//        SupplierHateoasWrapper wrapper = createSupplierWrapper();
//
//        when(mockWebClient.get()).thenReturn(mockRequestHeadersUriSpec);
//        when(mockRequestHeadersUriSpec.retrieve()).thenReturn(mockResponseSpec);
//        when(mockResponseSpec.bodyToMono(SupplierHateoasWrapper.class)).thenReturn(Mono.just(wrapper));
//
//        List<SupplierResponseModel> suppliers = client.getSuppliers();
//
//        assertNotNull(suppliers);
//        assertEquals(1, suppliers.size());
//        assertEquals("supplier1", suppliers.get(0).getSupplierId());
//    }
//
//    @Test
//    void getSuppliers_shouldReturnEmptyListIfNull() {
//        SupplierHateoasWrapper wrapper = new SupplierHateoasWrapper(); // no embedded suppliers
//
//        when(mockWebClient.get()).thenReturn(mockRequestHeadersUriSpec);
//        when(mockRequestHeadersUriSpec.retrieve()).thenReturn(mockResponseSpec);
//        when(mockResponseSpec.bodyToMono(SupplierHateoasWrapper.class)).thenReturn(Mono.just(wrapper));
//
//        List<SupplierResponseModel> suppliers = client.getSuppliers();
//
//        assertNotNull(suppliers);
//        assertTrue(suppliers.isEmpty());
//    }
//
//    @Test
//    void getSuppliers_shouldThrowNotFoundExceptionOn404() {
//        WebClientResponseException ex = WebClientResponseException.create(
//                404, "Not Found", null, "{\"message\":\"Not Found\"}".getBytes(), null);
//
//        when(mockWebClient.get()).thenReturn(mockRequestHeadersUriSpec);
//        when(mockRequestHeadersUriSpec.retrieve()).thenReturn(mockResponseSpec);
//        when(mockResponseSpec.bodyToMono(SupplierHateoasWrapper.class)).thenReturn(Mono.error(ex));
//
//        NotFoundException thrown = assertThrows(NotFoundException.class, () -> client.getSuppliers());
//        assertTrue(thrown.getMessage().contains("Not Found"));
//    }
//
//    @Test
//    void getSupplierById_shouldReturnSupplier() {
//        SupplierResponseModel supplier = createSampleSupplier();
//
//        when(mockWebClient.get()).thenReturn(mockRequestHeadersUriSpec);
//        when(mockRequestHeadersUriSpec.uri("/{id}", "supplier1")).thenReturn(mockRequestHeadersSpec);
//        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
//        when(mockResponseSpec.bodyToMono(SupplierResponseModel.class)).thenReturn(Mono.just(supplier));
//
//        SupplierResponseModel result = client.getSupplierById("supplier1");
//        assertEquals("supplier1", result.getSupplierId());
//    }
//
//    @Test
//    void addSupplier_shouldReturnSupplier() {
//        SupplierRequestModel request = new SupplierRequestModel();
//        request.setCompanyName("New Company");
//
//        SupplierResponseModel response = createSampleSupplier();
//
//        when(mockWebClient.post()).thenReturn(mockRequestBodyUriSpec);
//        when(mockRequestBodyUriSpec.bodyValue(request)).thenReturn(mockRequestBodySpec);
//        when(mockRequestBodySpec.retrieve()).thenReturn(mockResponseSpec);
//        when(mockResponseSpec.bodyToMono(SupplierResponseModel.class)).thenReturn(Mono.just(response));
//
//        SupplierResponseModel result = client.addSupplier(request);
//        assertEquals("supplier1", result.getSupplierId());
//    }
//
//    @Test
//    void updateSupplier_shouldReturnUpdatedSupplier() {
//        SupplierRequestModel request = new SupplierRequestModel();
//        request.setCompanyName("Updated Company");
//
//        SupplierResponseModel updatedSupplier = createSampleSupplier();
//
//        when(mockWebClient.put()).thenReturn(mockRequestBodyUriSpec);
//        when(mockRequestBodyUriSpec.uri("/{id}", "supplier1")).thenReturn(mockRequestBodySpec);
//        when(mockRequestBodySpec.bodyValue(request)).thenReturn(mockRequestBodySpec);
//        when(mockRequestBodySpec.retrieve()).thenReturn(mockResponseSpec);
//        when(mockResponseSpec.toBodilessEntity()).thenReturn(Mono.empty());
//
//        // Spy client to mock getSupplierById call after update
//        SupplierServiceClient spyClient = spy(client);
//        doReturn(updatedSupplier).when(spyClient).getSupplierById("supplier1");
//
//        SupplierResponseModel result = spyClient.updateSupplier("supplier1", request);
//
//        assertEquals("supplier1", result.getSupplierId());
//    }
//
//    @Test
//    void deleteSupplier_shouldReturnSuccessMessage() {
//        when(mockWebClient.delete()).thenReturn(mockRequestHeadersUriSpec);
//        when(mockRequestHeadersUriSpec.uri("/{id}", "supplier1")).thenReturn(mockRequestHeadersSpec);
//        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
//        when(mockResponseSpec.toBodilessEntity()).thenReturn(Mono.empty());
//
//        String message = client.deleteSupplier("supplier1");
//
//        assertEquals("Supplier deleted successfully.", message);
//    }
//
//    @Test
//    void handleWebClientException_shouldReturnNotFoundException() {
//        WebClientResponseException ex = WebClientResponseException.create(
//                404, "Not Found", null, "Not Found".getBytes(), null);
//
//        RuntimeException thrown = client.handleWebClientException(ex);
//
//        assertTrue(thrown instanceof NotFoundException);
//        assertEquals("Not Found", thrown.getMessage());
//    }
//
//    @Test
//    void handleWebClientException_shouldReturnInvalidInputException() {
//        WebClientResponseException ex = WebClientResponseException.create(
//                422, "Invalid Input", null, "Invalid input".getBytes(), null);
//
//        RuntimeException thrown = client.handleWebClientException(ex);
//
//        assertTrue(thrown instanceof InvalidInputException);
//        assertEquals("Invalid input", thrown.getMessage());
//    }
//
//    @Test
//    void handleWebClientException_shouldReturnOriginalException() {
//        WebClientResponseException ex = WebClientResponseException.create(
//                500, "Server Error", null, "Error".getBytes(), null);
//
//        RuntimeException thrown = client.handleWebClientException(ex);
//
//        assertEquals(ex, thrown);
//    }
//}
