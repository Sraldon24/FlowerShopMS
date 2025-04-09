package com.champsoft.services.inventory.Client;


import com.champsoft.services.inventory.PresentationLayer.supplierdtos.SupplierResponseModel;
import com.champsoft.services.inventory.utils.HttpErrorInfo;
import com.champsoft.services.inventory.utils.InvalidInputException;
import com.champsoft.services.inventory.utils.NotFoundException;
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
public class SuppliersServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String SUPPLIER_SERVICE_BASE_URL;

    public SuppliersServiceClient(RestTemplate restTemplate,
                                  ObjectMapper mapper,
                                  @Value("${spring.app.suppliers-service.host}") String supplierHost,
                                  @Value("${spring.app.suppliers-service.port}") String supplierPort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.SUPPLIER_SERVICE_BASE_URL = "http://" + supplierHost + ":" + supplierPort + "/api/v1/suppliers";
    }

    public SupplierResponseModel getSupplierBySupplierId(String supplierId) {
        try {
            return restTemplate.getForObject(
                    SUPPLIER_SERVICE_BASE_URL + "/" + supplierId,
                    SupplierResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        try {
            String errorMessage = mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
            if (ex.getStatusCode() == NOT_FOUND) return new NotFoundException(errorMessage);
            if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) return new InvalidInputException(errorMessage);
        } catch (IOException ignored) {
        }
        return ex;
    }
}
