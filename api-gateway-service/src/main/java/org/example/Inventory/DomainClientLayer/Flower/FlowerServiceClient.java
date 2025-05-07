package org.example.Inventory.DomainClientLayer.Flower;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.example.Inventory.PresentationLayer.Flower.FlowerHateoasWrapper;
import org.example.Inventory.PresentationLayer.Flower.FlowerRequestModel;
import org.example.Inventory.PresentationLayer.Flower.FlowerResponseModel;
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
public class FlowerServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String FLOWER_BASE_URL;

    public FlowerServiceClient(RestTemplate restTemplate,
                               ObjectMapper mapper,
                               @Value("${app.inventory-service.host}") String host,
                               @Value("${app.inventory-service.port}") String port) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.FLOWER_BASE_URL = "http://" + host + ":" + port + "/api/v1/flowers";
    }

    public List<FlowerResponseModel> getFlowers() {
        try {
            FlowerHateoasWrapper response = restTemplate.getForObject(
                    FLOWER_BASE_URL,
                    FlowerHateoasWrapper.class
            );
            return response != null && response.getEmbedded() != null
                    ? response.getEmbedded().getFlowerResponseModelList()
                    : List.of();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public FlowerResponseModel getFlowerById(String flowerId) {
        try {
            return restTemplate.getForObject(FLOWER_BASE_URL + "/" + flowerId, FlowerResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public FlowerResponseModel addFlower(FlowerRequestModel requestModel) {
        try {
            return restTemplate.postForObject(FLOWER_BASE_URL, requestModel, FlowerResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public FlowerResponseModel updateFlower(String flowerId, FlowerRequestModel requestModel) {
        try {
            restTemplate.put(FLOWER_BASE_URL + "/" + flowerId, requestModel);
            return getFlowerById(flowerId);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public String deleteFlower(String flowerId) {
        try {
            restTemplate.delete(FLOWER_BASE_URL + "/" + flowerId);
            return "Flower deleted successfully.";
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