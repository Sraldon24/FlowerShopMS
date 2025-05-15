package com.champsoft.services.sales.Client;

import com.champsoft.services.sales.PresentationLayer.employeedtos.EmployeeResponseModel;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeesServiceClientUnitTest {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private EmployeesServiceClient client;

    private final String baseUrl = "http://localhost:8080/employees";

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        objectMapper = mock(ObjectMapper.class);
        client = new EmployeesServiceClient(restTemplate, objectMapper, "http://localhost:8080");
    }

    @Test
    void getEmployeeById_success() {
        String employeeId = "123";
        EmployeeResponseModel response = new EmployeeResponseModel();
        when(restTemplate.getForObject(eq(baseUrl + "/" + employeeId), eq(EmployeeResponseModel.class)))
                .thenReturn(response);

        assertEquals(response, client.getEmployeeByEmployeeId(employeeId));
    }

    @Test
    void getEmployeeById_notFound() throws Exception {
        testHttpError(HttpStatus.NOT_FOUND, new NotFoundException("Employee not found"));
    }

    @Test
    void getEmployeeById_invalidInput() throws Exception {
        testHttpError(HttpStatus.UNPROCESSABLE_ENTITY, new InvalidInputException("Invalid input"));
    }

    @Test
    void getEmployeeById_fallbackToDefaultException() throws Exception {
        String json = "{}";
        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "", null, json.getBytes(), null);

        // Use doThrow instead of when(...).thenThrow(...) for checked exceptions
        doThrow(new com.fasterxml.jackson.core.JsonProcessingException("Mocked exception") {}).when(objectMapper).readValue(anyString(), eq(HttpErrorInfo.class));
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseModel.class))).thenThrow(ex);

        assertThrows(HttpClientErrorException.class, () -> client.getEmployeeByEmployeeId("id"));
    }

    private void testHttpError(HttpStatus status, RuntimeException expected) throws Exception {
        String json = "{\"message\":\"" + expected.getMessage() + "\"}";
        HttpClientErrorException ex = HttpClientErrorException.create(status, "", null, json.getBytes(), null);

        when(objectMapper.readValue(json, HttpErrorInfo.class))
                .thenReturn(new HttpErrorInfo(status, "uri", expected.getMessage()));
        when(restTemplate.getForObject(anyString(), eq(EmployeeResponseModel.class)))
                .thenThrow(ex);

        RuntimeException thrown = assertThrows(expected.getClass(), () -> client.getEmployeeByEmployeeId("id"));
        assertEquals(expected.getMessage(), thrown.getMessage());
    }
}
