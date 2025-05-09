package com.champsoft.services.sales.Client;

import com.champsoft.services.sales.PresentationLayer.paymentdtos.PaymentResponseModel;
import com.champsoft.services.sales.utils.HttpErrorInfo;
import com.champsoft.services.sales.utils.InvalidInputException;
import com.champsoft.services.sales.utils.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.springframework.beans.factory.annotation.Value;

@Component
@Slf4j
public class PaymentsServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String baseUrl;

    public PaymentsServiceClient(RestTemplate restTemplate,
                                 ObjectMapper mapper,
                                 @Value("${payment-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.baseUrl = baseUrl;
    }

    public PaymentResponseModel getPaymentById(String paymentId) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/" + paymentId,
                    PaymentResponseModel.class
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
