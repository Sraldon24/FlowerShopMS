package com.champsoft.services.sales.Client;

import com.champsoft.services.sales.PresentationLayer.inventorydtos.FlowerRequestModel;
import com.champsoft.services.sales.PresentationLayer.inventorydtos.FlowerResponseModel;
import com.champsoft.services.sales.PresentationLayer.inventorydtos.InventoryResponseModel;
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
class InventoryServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private InventoryServiceClient client;

    private final String baseUrl = "http://localhost:8080/inventories";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        client = new InventoryServiceClient(restTemplate, objectMapper, "http://localhost:8080");
    }

    @Test
    void getInventoryById_success() {
        String inventoryId = "inv1";
        InventoryResponseModel response = new InventoryResponseModel();

        when(restTemplate.getForObject(eq(baseUrl + "/" + inventoryId), eq(InventoryResponseModel.class)))
                .thenReturn(response);

        assertEquals(response, client.getInventoryById(inventoryId));
    }

    @Test
    void getFlowerByFlowerId_success() {
        String inventoryId = "inv1";
        String flowerId = "f1";
        FlowerResponseModel response = new FlowerResponseModel();

        when(restTemplate.getForObject(eq(baseUrl + "/" + inventoryId + "/flowers/" + flowerId), eq(FlowerResponseModel.class)))
                .thenReturn(response);

        assertEquals(response, client.getFlowerByFlowerId(inventoryId, flowerId));
    }

    @Test
    void updateFlowerStatus_success() {
        FlowerRequestModel request = new FlowerRequestModel();
        String inventoryId = "inv1";
        String flowerId = "f1";

        doNothing().when(restTemplate).put(eq(baseUrl + "/" + inventoryId + "/flowers/" + flowerId), eq(request));

        assertDoesNotThrow(() -> client.updateFlowerStatus(request, inventoryId, flowerId));
    }

    @Test
    void getInventoryById_notFound() throws Exception {
        testHttpError(HttpStatus.NOT_FOUND, new NotFoundException("Not Found"));
    }

    @Test
    void getInventoryById_invalidInput() throws Exception {
        testHttpError(HttpStatus.UNPROCESSABLE_ENTITY, new InvalidInputException("Invalid Input"));
    }

    @Test
    void getInventoryById_fallbackToDefaultException() throws Exception {
        String json = "{}";
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "", null, json.getBytes(), null);

        doThrow(new com.fasterxml.jackson.core.JsonProcessingException("Mocked exception") {}).when(objectMapper)
                .readValue(anyString(), eq(HttpErrorInfo.class));
        when(restTemplate.getForObject(anyString(), eq(InventoryResponseModel.class)))
                .thenThrow(ex);

        assertThrows(HttpClientErrorException.class, () -> client.getInventoryById("x"));
    }

    private void testHttpError(HttpStatus status, RuntimeException expected) throws Exception {
        String json = "{\"message\":\"" + expected.getMessage() + "\"}";
        HttpClientErrorException ex = HttpClientErrorException.create(status, "", null, json.getBytes(), null);

        when(objectMapper.readValue(json, HttpErrorInfo.class))
                .thenReturn(new HttpErrorInfo(status, "uri", expected.getMessage()));
        when(restTemplate.getForObject(anyString(), eq(InventoryResponseModel.class)))
                .thenThrow(ex);

        RuntimeException thrown = assertThrows(expected.getClass(), () -> client.getInventoryById("id"));
        assertEquals(expected.getMessage(), thrown.getMessage());
    }
}
