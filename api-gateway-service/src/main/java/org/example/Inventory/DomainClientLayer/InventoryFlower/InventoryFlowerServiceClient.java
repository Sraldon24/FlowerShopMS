package org.example.Inventory.DomainClientLayer.InventoryFlower;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.example.Inventory.PresentationLayer.InventoryFlower.InventoryFlowerResponseModel;
import org.example.Payment.Utils.HttpErrorInfo;
import org.example.Payment.Utils.InvalidInputException;
import org.example.Payment.Utils.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class InventoryFlowerServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String inventoryServiceBaseUrl;

    public InventoryFlowerServiceClient(
            RestTemplate restTemplate,
            ObjectMapper mapper,
            @Value("${app.inventory-service.host}") String host,
            @Value("${app.inventory-service.port}") String port) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.inventoryServiceBaseUrl = "http://" + host + ":" + port + "/api/v1/inventories";
    }

    public List<FlowerResponseModel> getFlowersInInventory(String inventoryId) {
        try {
            FlowerResponseModel[] flowers = restTemplate.getForObject(
                    inventoryServiceBaseUrl + "/" + inventoryId + "/flowers",
                    FlowerResponseModel[].class
            );
            return flowers != null ? Arrays.asList(flowers) : List.of();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public FlowerResponseModel getFlowerInInventoryById(String inventoryId, String flowerId) {
        try {
            return restTemplate.getForObject(
                    inventoryServiceBaseUrl + "/" + inventoryId + "/flowers/" + flowerId,
                    FlowerResponseModel.class
            );
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
