package org.example.Sales.DomainClientLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Sales.PresentationLayer.PurchaseRequestModel;
import org.example.Sales.PresentationLayer.PurchaseResponseModel;
import org.example.Sales.Utils.HttpErrorInfo;
import org.example.Sales.Utils.InvalidInputException;
import org.example.Sales.Utils.NotFoundException;
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
@RequiredArgsConstructor
public class PurchaseServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${app.purchase-service.base-url}")
    private String PURCHASE_SERVICE_BASE_URL;

    public List<PurchaseResponseModel> getAllPurchases() {
        try {
            PurchaseResponseModel[] responses = restTemplate.getForObject(
                    PURCHASE_SERVICE_BASE_URL + "/purchases", PurchaseResponseModel[].class);
            return Arrays.asList(responses);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PurchaseResponseModel getPurchaseById(String id) {
        try {
            return restTemplate.getForObject(
                    PURCHASE_SERVICE_BASE_URL + "/purchases/" + id, PurchaseResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PurchaseResponseModel createPurchase(PurchaseRequestModel model) {
        try {
            return restTemplate.postForObject(
                    PURCHASE_SERVICE_BASE_URL + "/purchases", model, PurchaseResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PurchaseResponseModel updatePurchase(String id, PurchaseRequestModel model) {
        try {
            restTemplate.put(PURCHASE_SERVICE_BASE_URL + "/purchases/" + id, model);
            return getPurchaseById(id); // Fetch updated
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deletePurchase(String id) {
        try {
            restTemplate.delete(PURCHASE_SERVICE_BASE_URL + "/purchases/" + id);
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
