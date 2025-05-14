package org.example.Sales.DomainClientLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.Sales.PresentationLayer.PurchaseRequestModel;
import org.example.Sales.PresentationLayer.PurchaseResponseModel;
import org.example.Sales.Utils.HttpErrorInfo;
import org.example.Sales.Utils.InvalidInputException;
import org.example.Sales.Utils.LowSalePriceException;
import org.example.Sales.Utils.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PurchaseRequestModel> request = new HttpEntity<>(model, headers);

            ResponseEntity<PurchaseResponseModel> response = restTemplate.exchange(
                    PURCHASE_SERVICE_BASE_URL + "/purchases",
                    HttpMethod.POST,
                    request,
                    PurchaseResponseModel.class
            );
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PurchaseResponseModel updatePurchase(String id, PurchaseRequestModel model) {
        try {
            restTemplate.put(PURCHASE_SERVICE_BASE_URL + "/purchases/" + id, model);
            return getPurchaseById(id);
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
        String rawBody = ex.getResponseBodyAsString();
        String cleanJson = rawBody;

        try {
            // 🔍 Case 1: if the raw body is a wrapped JSON string like "{\"path\":...}"
            if (rawBody.startsWith("\"{") && rawBody.endsWith("}\"")) {
                cleanJson = mapper.readValue(rawBody, String.class); // Unwrap to real JSON
            }

            // 🧠 Deserialize into real object
            HttpErrorInfo errorInfo = mapper.readValue(cleanJson, HttpErrorInfo.class);

            String errorMessage = errorInfo.getMessage();

            // 🔥 Map specific known errors
            if (errorMessage.contains("Sale price must be at least")) {
                return new LowSalePriceException(errorMessage);
            }

            if (errorInfo.getHttpStatus() == HttpStatus.NOT_FOUND) {
                return new NotFoundException(errorMessage);
            }

            if (errorInfo.getHttpStatus() == HttpStatus.UNPROCESSABLE_ENTITY) {
                return new InvalidInputException(errorMessage);
            }

            return new InvalidInputException(errorMessage);

        } catch (Exception e) {
            // 🧯 Fallback: throw as plain text
            return new InvalidInputException("Unknown error: " + rawBody);
        }
    }


}
