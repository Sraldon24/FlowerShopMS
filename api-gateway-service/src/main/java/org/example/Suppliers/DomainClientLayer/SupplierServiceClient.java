package org.example.Suppliers.DomainClientLayer;

import lombok.extern.slf4j.Slf4j;
import org.example.Suppliers.PresentationLayer.SupplierHateoasWrapper;
import org.example.Suppliers.PresentationLayer.SupplierRequestModel;
import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
import org.example.Suppliers.Utils.HttpErrorInfo;
import org.example.Suppliers.Utils.InvalidInputException;
import org.example.Suppliers.Utils.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class SupplierServiceClient {

    private final WebClient webClient;

    public SupplierServiceClient(@Value("${app.suppliers-service.host}") String host,
                                 @Value("${app.suppliers-service.port}") String port) {
        this.webClient = WebClient.builder()
                .baseUrl("http://" + host + ":" + port + "/api/v1/suppliers")
                .build();
    }

    public List<SupplierResponseModel> getSuppliers() {
        try {
            SupplierHateoasWrapper wrapper = webClient.get()
                    .retrieve()
                    .bodyToMono(SupplierHateoasWrapper.class)
                    .block();

            return wrapper != null && wrapper.getEmbedded() != null
                    ? wrapper.getEmbedded().getSupplierList()
                    : List.of();
        } catch (WebClientResponseException ex) {
            throw handleWebClientException(ex);
        }
    }

    public SupplierResponseModel getSupplierById(String supplierId) {
        try {
            return webClient.get()
                    .uri("/{id}", supplierId)
                    .retrieve()
                    .bodyToMono(SupplierResponseModel.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw handleWebClientException(ex);
        }
    }

    public SupplierResponseModel addSupplier(SupplierRequestModel model) {
        try {
            return webClient.post()
                    .bodyValue(model)
                    .retrieve()
                    .bodyToMono(SupplierResponseModel.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw handleWebClientException(ex);
        }
    }

    public SupplierResponseModel updateSupplier(String supplierId, SupplierRequestModel model) {
        try {
            webClient.put()
                    .uri("/{id}", supplierId)
                    .bodyValue(model)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return getSupplierById(supplierId);
        } catch (WebClientResponseException ex) {
            throw handleWebClientException(ex);
        }
    }

    public String deleteSupplier(String supplierId) {
        try {
            webClient.delete()
                    .uri("/{id}", supplierId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            return "Supplier deleted successfully.";
        } catch (WebClientResponseException ex) {
            throw handleWebClientException(ex);
        }
    }

    private RuntimeException handleWebClientException(WebClientResponseException ex) {
        try {
            String errorMessage = Objects.requireNonNull(ex.getResponseBodyAsString());

            if (ex.getStatusCode() == HttpStatusCode.valueOf(404)) {
                return new NotFoundException(errorMessage);
            } else if (ex.getStatusCode() == HttpStatusCode.valueOf(422)) {
                return new InvalidInputException(errorMessage);
            }
        } catch (Exception ignored) {}
        return ex;
    }
}