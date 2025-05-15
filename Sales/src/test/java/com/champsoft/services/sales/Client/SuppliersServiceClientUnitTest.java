package com.champsoft.services.sales.Client;

import com.champsoft.services.sales.PresentationLayer.supplierdtos.SupplierResponseModel;
import com.champsoft.services.sales.utils.HttpErrorInfo;
import com.champsoft.services.sales.utils.InvalidInputException;
import com.champsoft.services.sales.utils.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuppliersServiceClientTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private SuppliersServiceClient client;

    private final String baseUrl = "http://localhost:8080/suppliers";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        client = new SuppliersServiceClient(restTemplate, objectMapper, "http://localhost:8080");
    }

    @Test
    void getSupplierById_success() {
        String supplierId = "supp1";
        SupplierResponseModel response = new SupplierResponseModel();

        when(restTemplate.getForObject(eq(baseUrl + "/" + supplierId), eq(SupplierResponseModel.class)))
                .thenReturn(response);

        assertEquals(response, client.getSupplierBySupplierId(supplierId));
    }

    @Test
    void getSupplierById_notFound() throws Exception {
        testHttpError(HttpStatus.NOT_FOUND, new NotFoundException("Not found"));
    }

    @Test
    void getSupplierById_invalidInput() throws Exception {
        testHttpError(HttpStatus.UNPROCESSABLE_ENTITY, new InvalidInputException("Invalid input"));
    }

    @Test
    void getSupplierById_fallbackToDefaultException() throws Exception {
        String json = "{}";
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "", null, json.getBytes(), null);

        doThrow(new com.fasterxml.jackson.core.JsonProcessingException("Mocked exception") {}).when(objectMapper)
                .readValue(anyString(), eq(HttpErrorInfo.class));
        when(restTemplate.getForObject(anyString(), eq(SupplierResponseModel.class)))
                .thenThrow(ex);

        assertThrows(HttpClientErrorException.class, () -> client.getSupplierBySupplierId("x"));
    }

    private void testHttpError(HttpStatus status, RuntimeException expected) throws Exception {
        String json = "{\"message\":\"" + expected.getMessage() + "\"}";
        HttpClientErrorException ex = HttpClientErrorException.create(status, "", null, json.getBytes(), null);

        when(objectMapper.readValue(json, HttpErrorInfo.class))
                .thenReturn(new HttpErrorInfo(status, "uri", expected.getMessage()));
        when(restTemplate.getForObject(anyString(), eq(SupplierResponseModel.class)))
                .thenThrow(ex);

        RuntimeException thrown = assertThrows(expected.getClass(), () -> client.getSupplierBySupplierId("id"));
        assertEquals(expected.getMessage(), thrown.getMessage());
    }
}
