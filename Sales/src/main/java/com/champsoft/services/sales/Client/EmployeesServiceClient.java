/* ======================================
   EmployeesServiceClient.java
   ====================================== */
package com.champsoft.services.sales.Client;


import com.champsoft.services.sales.PresentationLayer.employeedtos.EmployeeResponseModel;
import com.champsoft.services.sales.utils.HttpErrorInfo;
import com.champsoft.services.sales.utils.InvalidInputException;
import com.champsoft.services.sales.utils.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class EmployeesServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String EMPLOYEE_SERVICE_BASE_URL;

    public EmployeesServiceClient(RestTemplate restTemplate,
                                  ObjectMapper mapper,
                                  @Value("${app.employees-service.host}") String employeeHost,
                                  @Value("${app.employees-service.port}") String employeePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.EMPLOYEE_SERVICE_BASE_URL = "http://" + employeeHost + ":" + employeePort + "/api/v1/employees";
    }

    public EmployeeResponseModel getEmployeeByEmployeeId(String employeeId) {
        try {
            return restTemplate.getForObject(
                    EMPLOYEE_SERVICE_BASE_URL + "/" + employeeId,
                    EmployeeResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        try {
            String message = mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
            if (ex.getStatusCode() == NOT_FOUND) return new NotFoundException(message);
            if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) return new InvalidInputException(message);
        } catch (IOException ignored) {
        }
        return ex;
    }
}

