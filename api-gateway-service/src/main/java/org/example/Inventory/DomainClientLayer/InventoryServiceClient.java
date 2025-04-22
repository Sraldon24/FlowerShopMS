package org.example.Inventory.DomainClientLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.Inventory.PresentationLayer.InventoryHateoasWrapper;
import org.example.Inventory.PresentationLayer.InventoryRequestModel;
import org.example.Inventory.PresentationLayer.InventoryResponseModel;
import org.example.Payment.Utils.HttpErrorInfo;
import org.example.Payment.Utils.InvalidInputException;
import org.example.Payment.Utils.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class InventoryServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String INVENTORY_BASE_URL;

    public InventoryServiceClient(RestTemplate restTemplate,
                                  ObjectMapper mapper,
                                  @Value("${app.inventory-service.host}") String host,
                                  @Value("${app.inventory-service.port}") String port) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.INVENTORY_BASE_URL = "http://" + host + ":" + port + "/api/v1/inventories";
    }

    public List<InventoryResponseModel> getInventories() {
        try {
            InventoryHateoasWrapper response = restTemplate.getForObject(
                    INVENTORY_BASE_URL,
                    InventoryHateoasWrapper.class
            );
            return response != null && response.getEmbedded() != null
                    ? response.getEmbedded().getInventoryList()
                    : List.of();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public InventoryResponseModel getInventoryById(String id) {
        try {
            return restTemplate.getForObject(INVENTORY_BASE_URL + "/" + id, InventoryResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public InventoryResponseModel addInventory(InventoryRequestModel requestModel) {
        try {
            return restTemplate.postForObject(INVENTORY_BASE_URL, requestModel, InventoryResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public InventoryResponseModel updateInventory(String id, InventoryRequestModel requestModel) {
        try {
            restTemplate.put(INVENTORY_BASE_URL + "/" + id, requestModel);
            return getInventoryById(id);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public String deleteInventory(String id) {
        try {
            restTemplate.delete(INVENTORY_BASE_URL + "/" + id);
            return "Inventory deleted successfully.";
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
