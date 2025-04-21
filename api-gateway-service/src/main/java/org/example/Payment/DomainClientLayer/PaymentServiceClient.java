package org.example.Payment.DomainClientLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.example.Payment.PresentationLayer.PaymentHateoasWrapper;
import org.example.Payment.PresentationLayer.PaymentRequestModel;
import org.example.Payment.PresentationLayer.PaymentResponseModel;
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
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String PAYMENT_SERVICE_BASE_URL;

    public PaymentServiceClient(RestTemplate restTemplate,
                                ObjectMapper mapper,
                                @Value("${app.payment-service.host}") String host,
                                @Value("${app.payment-service.port}") String port) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.PAYMENT_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/payments";
    }

    public List<PaymentResponseModel> getPayments() {
        try {
            PaymentHateoasWrapper response = restTemplate.getForObject(
                    PAYMENT_SERVICE_BASE_URL,
                    PaymentHateoasWrapper.class
            );
            return response != null && response.getEmbedded() != null
                    ? response.getEmbedded().getPaymentList()
                    : List.of();
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PaymentResponseModel getPaymentById(String paymentId) {
        try {
            return restTemplate.getForObject(
                    PAYMENT_SERVICE_BASE_URL + "/" + paymentId,
                    PaymentResponseModel.class
            );
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PaymentResponseModel addPayment(PaymentRequestModel model) {
        try {
            return restTemplate.postForObject(PAYMENT_SERVICE_BASE_URL, model, PaymentResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public PaymentResponseModel updatePayment(String paymentId, PaymentRequestModel model) {
        try {
            restTemplate.put(PAYMENT_SERVICE_BASE_URL + "/" + paymentId, model);
            return getPaymentById(paymentId);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public String deletePayment(String paymentId) {
        try {
            restTemplate.delete(PAYMENT_SERVICE_BASE_URL + "/" + paymentId);
            return "Payment deleted successfully.";
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


