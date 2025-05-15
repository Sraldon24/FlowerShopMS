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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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


    @Test
    public void testGetInventoryById_Success() {
        InventoryResponseModel mockResponse = new InventoryResponseModel();
        when(restTemplate.getForObject(anyString(), eq(InventoryResponseModel.class))).thenReturn(mockResponse);

        InventoryResponseModel result = client.getInventoryById("123");
        assertEquals(mockResponse, result);
    }

    @Test
    public void testGetFlowerByFlowerId_Success() {
        FlowerResponseModel mockResponse = new FlowerResponseModel();
        when(restTemplate.getForObject(anyString(), eq(FlowerResponseModel.class))).thenReturn(mockResponse);

        FlowerResponseModel result = client.getFlowerByFlowerId("inv1", "flw1");
        assertEquals(mockResponse, result);
    }

    @Test
    public void testUpdateFlowerStatus_Success() {
        FlowerRequestModel model = new FlowerRequestModel();
        doNothing().when(restTemplate).put(anyString(), eq(model));

        assertDoesNotThrow(() -> client.updateFlowerStatus(model, "inv1", "flw1"));
    }

//    @Test
//    public void testHandleHttpClientException_NotFound() throws IOException {
//        String errorMessage = "Not Found";
//        HttpErrorInfo errorInfo = new HttpErrorInfo();
//        errorInfo.setMessage(errorMessage);
//
//        HttpClientErrorException ex = HttpClientErrorException.create(
//                HttpStatus.NOT_FOUND, "Not Found", null, "{\"message\":\"" + errorMessage + "\"}".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
//
//        when(objectMapper.readValue(anyString(), eq(HttpErrorInfo.class))).thenReturn(errorInfo);
//
//        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
//            throw client.getInventoryById("404");
//        });
//
//        assertEquals(errorMessage, thrown.getMessage());
//    }
//
//    @Test
//    public void testHandleHttpClientException_UnprocessableEntity() throws IOException {
//        String errorMessage = "Invalid input";
//        HttpErrorInfo errorInfo = new HttpErrorInfo();
//        errorInfo.setMessage(errorMessage);
//
//        HttpClientErrorException ex = HttpClientErrorException.create(
//                HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable", null, ("{\"message\":\"" + errorMessage + "\"}").getBytes(), StandardCharsets.UTF_8);
//
//        when(restTemplate.getForObject(anyString(), eq(InventoryResponseModel.class))).thenThrow(ex);
//        when(objectMapper.readValue(anyString(), eq(HttpErrorInfo.class))).thenReturn(errorInfo);
//
//        InvalidInputException thrown = assertThrows(InvalidInputException.class, () -> {
//            client.getInventoryById("bad");
//        });
//
//        assertEquals(errorMessage, thrown.getMessage());
//    }

//    @Test
//    public void testHandleHttpClientException_FallbackToOriginalException() throws IOException {
//        HttpClientErrorException ex = HttpClientErrorException.create(
//                HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, "Server error".getBytes(), StandardCharsets.UTF_8);
//
//        when(objectMapper.readValue(anyString(), eq(HttpErrorInfo.class))).thenThrow(new IOException("Parsing error"));
//
//        HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class, () -> {
//            client.getInventoryById("fail");
//        });
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatusCode());
//    }
}
