package org.example.Suppliers.DomainClientLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.Suppliers.PresentationLayer.SupplierHateoasWrapper;
import org.example.Suppliers.PresentationLayer.SupplierRequestModel;
import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
import org.example.Suppliers.Utils.HttpErrorInfo;
import org.example.Suppliers.Utils.InvalidInputException;
import org.example.Suppliers.Utils.NotFoundException;
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
public class SupplierServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String SUPPLIER_SERVICE_BASE_URL;

    public SupplierServiceClient(RestTemplate restTemplate,
                                 ObjectMapper mapper,
                                 @Value("${app.suppliers-service.host}") String host,
                                 @Value("${app.suppliers-service.port}") String port) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.SUPPLIER_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/suppliers";
    }

    public List<SupplierResponseModel> getSuppliers() {
        try {
            SupplierHateoasWrapper response = restTemplate.getForObject(
                    SUPPLIER_SERVICE_BASE_URL,
                    SupplierHateoasWrapper.class
            );
            return response != null && response.getEmbedded() != null
                    ? response.getEmbedded().getSupplierList()
                    : List.of();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public SupplierResponseModel getSupplierById(String supplierId) {
        try {
            return restTemplate.getForObject(
                    SUPPLIER_SERVICE_BASE_URL + "/" + supplierId,
                    SupplierResponseModel.class
            );
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public SupplierResponseModel addSupplier(SupplierRequestModel model) {
        try {
            return restTemplate.postForObject(SUPPLIER_SERVICE_BASE_URL, model, SupplierResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public SupplierResponseModel updateSupplier(String supplierId, SupplierRequestModel model) {
        try {
            restTemplate.put(SUPPLIER_SERVICE_BASE_URL + "/" + supplierId, model);
            return getSupplierById(supplierId);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public String deleteSupplier(String supplierId) {
        try {
            restTemplate.delete(SUPPLIER_SERVICE_BASE_URL + "/" + supplierId);
            return "Supplier deleted successfully.";
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
