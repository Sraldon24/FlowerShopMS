package com.champsoft.services.sales.Client;

import com.champsoft.services.sales.PresentationLayer.employeedtos.EmployeeResponseModel;
import com.champsoft.services.sales.PresentationLayer.inventorydtos.FlowerResponseModel;
import com.champsoft.services.sales.PresentationLayer.supplierdtos.SupplierResponseModel;
import com.champsoft.services.sales.utils.HttpErrorInfo;
import com.champsoft.services.sales.utils.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientLayerTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    private SuppliersServiceClient suppliersServiceClient;
    private EmployeesServiceClient employeesServiceClient;
    private InventoryServiceClient inventoryServiceClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        String baseUrl = "http://api-gateway:8080/api/v1";
        suppliersServiceClient = new SuppliersServiceClient(restTemplate, objectMapper, baseUrl);
        employeesServiceClient = new EmployeesServiceClient(restTemplate, objectMapper, baseUrl);
        inventoryServiceClient = new InventoryServiceClient(restTemplate, objectMapper, baseUrl);
    }

    // === SUPPLIER SERVICE ===

    @Test
    void testGetSupplierById_success() {
        SupplierResponseModel mockSupplier = new SupplierResponseModel();
        when(restTemplate.getForObject(anyString(), eq(SupplierResponseModel.class))).thenReturn(mockSupplier);

        SupplierResponseModel result = suppliersServiceClient.getSupplierBySupplierId("sup-123");
        assertNotNull(result);
    }

    @Test
    void testGetSupplierById_notFound() {
        String fullUrl = "http://api-gateway:8080/api/v1/suppliers/sup-404";
        String errorJson = """
            {
              "timestamp": "2025-04-20T12:00:00Z",
              "path": "/suppliers/sup-404",
              "httpStatus": "NOT_FOUND",
              "message": "Supplier not found"
            }
        """;
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", null,
                errorJson.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );

        when(restTemplate.getForObject(eq(fullUrl), eq(SupplierResponseModel.class))).thenThrow(ex);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                suppliersServiceClient.getSupplierBySupplierId("sup-404"));

        assertEquals("Supplier not found", exception.getMessage());
    }

    // === EMPLOYEE SERVICE ===

    @Test
    void testGetEmployeeById_success() {
        EmployeeResponseModel mockEmployee = new EmployeeResponseModel();
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseModel.class))).thenReturn(mockEmployee);

        EmployeeResponseModel result = employeesServiceClient.getEmployeeByEmployeeId("emp-123");
        assertNotNull(result);
    }

    @Test
    void testGetEmployeeById_notFound() {
        String fullUrl = "http://api-gateway:8080/api/v1/employees/emp-404";
        String errorJson = """
            {
              "timestamp": "2025-04-20T12:00:00Z",
              "path": "/employees/emp-404",
              "httpStatus": "NOT_FOUND",
              "message": "Employee not found"
            }
        """;
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", null,
                errorJson.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );

        when(restTemplate.getForObject(eq(fullUrl), eq(EmployeeResponseModel.class))).thenThrow(ex);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                employeesServiceClient.getEmployeeByEmployeeId("emp-404"));

        assertEquals("Employee not found", exception.getMessage());
    }

    // === INVENTORY SERVICE ===

    @Test
    void testGetFlowerById_success() {
        FlowerResponseModel mockFlower = new FlowerResponseModel();
        when(restTemplate.getForObject(anyString(), eq(FlowerResponseModel.class))).thenReturn(mockFlower);

        FlowerResponseModel result = inventoryServiceClient.getFlowerByFlowerId("inv-1", "fl-1");
        assertNotNull(result);
    }

    @Test
    void testGetFlowerById_notFound() {
        String fullUrl = "http://api-gateway:8080/api/v1/inventories/inv-1/flowers/fl-404";
        String errorJson = """
            {
              "timestamp": "2025-04-20T12:00:00Z",
              "path": "/inventories/inv-1/flowers/fl-404",
              "httpStatus": "NOT_FOUND",
              "message": "Flower not found"
            }
        """;
        HttpClientErrorException ex = HttpClientErrorException.create(
                HttpStatus.NOT_FOUND, "Not Found", null,
                errorJson.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8
        );

        when(restTemplate.getForObject(eq(fullUrl), eq(FlowerResponseModel.class))).thenThrow(ex);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                inventoryServiceClient.getFlowerByFlowerId("inv-1", "fl-404"));

        assertEquals("Flower not found", exception.getMessage());
    }
}
