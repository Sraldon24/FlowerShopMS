package com.champsoft.services.sales.Client;

import com.champsoft.services.sales.PresentationLayer.inventorydtos.FlowerRequestModel;
import com.champsoft.services.sales.PresentationLayer.inventorydtos.FlowerResponseModel;
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
public class InventoryServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String INVENTORY_SERVICE_BASE_URL;

    public InventoryServiceClient(RestTemplate restTemplate,
                                  ObjectMapper mapper,
                                  @Value("${app.gateway.base-url}") String gatewayBaseUrl) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.INVENTORY_SERVICE_BASE_URL = gatewayBaseUrl + "/inventories"; // ✅ Fixed
    }

    public FlowerResponseModel getFlowerByFlowerId(String inventoryId, String flowerId) {
        String url = INVENTORY_SERVICE_BASE_URL + "/" + inventoryId + "/flowers/" + flowerId;
        try {
            return restTemplate.getForObject(url, FlowerResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void updateFlowerStatus(FlowerRequestModel model, String inventoryId, String flowerId) {
        String url = INVENTORY_SERVICE_BASE_URL + "/" + inventoryId + "/flowers/" + flowerId;
        try {
            restTemplate.put(url, model);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        try {
            String message = mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
            if (ex.getStatusCode() == NOT_FOUND) return new NotFoundException(message);
            if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) return new InvalidInputException(message);
        } catch (IOException ignored) {}
        return ex;
    }
}
