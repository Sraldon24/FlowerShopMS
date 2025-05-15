package org.example.Sales.DomainClientLayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.Sales.DataLayer.FinancingAgreementDetails;
import org.example.Sales.DataLayer.PurchaseStatus;
import org.example.Sales.PresentationLayer.*;
import org.example.Sales.PresentationLayer.inventorydtos.FlowerResponseModel;
import org.example.Sales.PresentationLayer.inventorydtos.InventoryResponseModel;
import org.example.Sales.PresentationLayer.paymentdtos.PaymentResponseModel;
import org.example.Sales.PresentationLayer.supplierdtos.SupplierResponseModel;
import org.example.Sales.Utils.HttpErrorInfo;
import org.example.Sales.Utils.InvalidInputException;
import org.example.Sales.Utils.LowSalePriceException;
import org.example.Sales.Utils.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PurchaseServiceClientTest {

    private RestTemplate restTemplate;
    private ObjectMapper mapper;
    private PurchaseServiceClient client;

    @BeforeEach
    void setup() throws Exception {
        restTemplate = mock(RestTemplate.class);


        Field field = PurchaseServiceClient.class.getDeclaredField("PURCHASE_SERVICE_BASE_URL");
        field.setAccessible(true);

        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        client = new PurchaseServiceClient(restTemplate, mapper);
        field.set(client, "http://localhost:8080");
    }

    @Test
    void testGetAllPurchases_success() {
        PurchaseResponseModel res1 = new PurchaseResponseModel(
                "order1", "inv1", new InventoryResponseModel(),
                "fl1", new FlowerResponseModel(),
                "sup1", new SupplierResponseModel(),
                "emp1", "pay1", new PaymentResponseModel(),
                BigDecimal.valueOf(100.50), "CAD",
                LocalDate.now(), PurchaseStatus.PENDING,
                new FinancingAgreementDetails()
        );

        PurchaseResponseModel res2 = new PurchaseResponseModel(
                "order2", "inv2", new InventoryResponseModel(),
                "fl2", new FlowerResponseModel(),
                "sup2", new SupplierResponseModel(),
                "emp2", "pay2", new PaymentResponseModel(),
                BigDecimal.valueOf(150.75), "USD",
                LocalDate.now(), PurchaseStatus.COMPLETED,
                new FinancingAgreementDetails()
        );

        when(restTemplate.getForObject(anyString(), eq(PurchaseResponseModel[].class)))
                .thenReturn(new PurchaseResponseModel[]{res1, res2});

        List<PurchaseResponseModel> result = client.getAllPurchases();

        assertEquals(2, result.size());
        assertEquals("order1", result.get(0).getPurchaseOrderId());
    }

    @Test
    void testGetPurchaseById_success() {
        PurchaseResponseModel response = new PurchaseResponseModel(
                "order1", "inv1", new InventoryResponseModel(),
                "fl1", new FlowerResponseModel(),
                "sup1", new SupplierResponseModel(),
                "emp1", "pay1", new PaymentResponseModel(),
                BigDecimal.valueOf(100.50), "CAD",
                LocalDate.now(), PurchaseStatus.PENDING,
                new FinancingAgreementDetails()
        );

        when(restTemplate.getForObject(anyString(), eq(PurchaseResponseModel.class))).thenReturn(response);

        PurchaseResponseModel result = client.getPurchaseById("order1");
        assertEquals("order1", result.getPurchaseOrderId());
        assertEquals("emp1", result.getEmployeeId());
        assertEquals("pay1", result.getPaymentId());
    }

    @Test
    void testCreatePurchase_success() {
        PurchaseRequestModel request = PurchaseRequestModel.builder()
                .inventoryId("inv1")
                .flowerIdentificationNumber("fl1")
                .supplierId("sup1")
                .employeeId("emp1")
                .paymentId("pay1")
                .salePrice(100.00)
                .currency("CAD")
                .payment_currency("USD")
                .saleOfferDate(LocalDate.now())
                .salePurchaseStatus(PurchaseStatus.PENDING)
                .financingAgreementDetails(new FinancingAgreementDetails())
                .build();

        PurchaseResponseModel response = new PurchaseResponseModel(
                "order1", "inv1", new InventoryResponseModel(),
                "fl1", new FlowerResponseModel(),
                "sup1", new SupplierResponseModel(),
                "emp1", "pay1", new PaymentResponseModel(),
                BigDecimal.valueOf(100.00), "CAD",
                LocalDate.now(), PurchaseStatus.PENDING,
                new FinancingAgreementDetails()
        );

        ResponseEntity<PurchaseResponseModel> entity = new ResponseEntity<>(response, HttpStatus.CREATED);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(PurchaseResponseModel.class)))
                .thenReturn(entity);

        PurchaseResponseModel result = client.createPurchase(request);
        assertEquals("order1", result.getPurchaseOrderId());
    }

    @Test
    void testUpdatePurchase_success() {
        PurchaseRequestModel request = PurchaseRequestModel.builder()
                .inventoryId("inv1")
                .flowerIdentificationNumber("fl1")
                .supplierId("sup1")
                .employeeId("emp1")
                .paymentId("pay1")
                .salePrice(200.00)
                .currency("CAD")
                .payment_currency("USD")
                .saleOfferDate(LocalDate.now())
                .salePurchaseStatus(PurchaseStatus.COMPLETED)
                .financingAgreementDetails(new FinancingAgreementDetails())
                .build();

        PurchaseResponseModel response = new PurchaseResponseModel(
                "order1", "inv1", new InventoryResponseModel(),
                "fl1", new FlowerResponseModel(),
                "sup1", new SupplierResponseModel(),
                "emp1", "pay1", new PaymentResponseModel(),
                BigDecimal.valueOf(200.00), "CAD",
                LocalDate.now(), PurchaseStatus.COMPLETED,
                new FinancingAgreementDetails()
        );

        doNothing().when(restTemplate).put(anyString(), eq(request));
        when(restTemplate.getForObject(anyString(), eq(PurchaseResponseModel.class))).thenReturn(response);

        PurchaseResponseModel result = client.updatePurchase("order1", request);
        assertEquals(BigDecimal.valueOf(200.00), result.getSalePrice());
        assertEquals(PurchaseStatus.COMPLETED, result.getSalePurchaseStatus());
    }

    @Test
    void testDeletePurchase_success() {
        doNothing().when(restTemplate).delete(anyString());
        assertDoesNotThrow(() -> client.deletePurchase("order1"));
    }

//    @Test
//    void testHandleHttpClientException_notFound() throws Exception {
//        HttpErrorInfo error = new HttpErrorInfo(HttpStatus.NOT_FOUND, "/p", "Not Found");
//        String json = mapper.writeValueAsString(error);
//        HttpClientErrorException ex = new HttpClientErrorException(
//                HttpStatus.NOT_FOUND,
//                "Not Found",
//                json.getBytes(StandardCharsets.UTF_8),
//                StandardCharsets.UTF_8
//        );
//
//        when(restTemplate.getForObject(anyString(), eq(PurchaseResponseModel.class))).thenThrow(ex);
//
//        assertThrows(NotFoundException.class, () -> client.getPurchaseById("id"));
//    }

//    @Test
//    void testHandleHttpClientException_lowSalePrice() throws Exception {
//        HttpErrorInfo error = new HttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, "/p", "Sale price must be at least 1.0");
//        String json = mapper.writeValueAsString(error);
//        HttpClientErrorException ex = new HttpClientErrorException(
//                HttpStatus.UNPROCESSABLE_ENTITY,
//                "Unprocessable",
//                json.getBytes(StandardCharsets.UTF_8),
//                StandardCharsets.UTF_8
//        );
//
//        when(restTemplate.getForObject(anyString(), eq(PurchaseResponseModel.class))).thenThrow(ex);
//
//        assertThrows(LowSalePriceException.class, () -> client.getPurchaseById("id"));
//    }

    @Test
    void testHandleHttpClientException_invalidJson() {
        HttpClientErrorException ex = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "invalid_json".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(restTemplate.getForObject(anyString(), eq(PurchaseResponseModel.class))).thenThrow(ex);

        assertThrows(InvalidInputException.class, () -> client.getPurchaseById("id"));
    }
}
