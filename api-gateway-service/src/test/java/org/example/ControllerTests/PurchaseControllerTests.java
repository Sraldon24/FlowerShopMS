package org.example.ControllerTests;


import org.example.Sales.BusinessLayer.PurchaseServiceImpl;
import org.example.Sales.DataLayer.FinancingAgreementDetails;
import org.example.Sales.DataLayer.PurchaseStatus;
import org.example.Sales.PresentationLayer.PurchaseController;
import org.example.Sales.PresentationLayer.PurchaseRequestModel;
import org.example.Sales.PresentationLayer.PurchaseResponseModel;
import org.example.Sales.PresentationLayer.inventorydtos.FlowerResponseModel;
import org.example.Sales.PresentationLayer.inventorydtos.InventoryResponseModel;
import org.example.Sales.PresentationLayer.paymentdtos.PaymentResponseModel;
import org.example.Sales.PresentationLayer.supplierdtos.SupplierResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseControllerTests {

    @Mock
    private PurchaseServiceImpl purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    private PurchaseRequestModel sampleRequestModel;
    private PurchaseResponseModel sampleResponseModel;
    private List<PurchaseResponseModel> sampleResponseList;
    private FinancingAgreementDetails financingDetails;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2023, 7, 15); // Fixed date to avoid test flakiness
        setupTestData();
    }

    private void setupTestData() {
        // Setup financing details
        financingDetails = new FinancingAgreementDetails();
        financingDetails.setDownPaymentAmount(20000.0);
        financingDetails.setNumberOfMonthlyPayments(45);
        financingDetails.setMonthlyPaymentAmount(480.0);

        // Setup sample request model
        sampleRequestModel = PurchaseRequestModel.builder()
                .purchaseOrderId("po123")
                .inventoryId("inv123")
                .flowerIdentificationNumber("FIN123456")
                .supplierId("sup123")
                .employeeId("emp123")
                .paymentId("pay123")
                .salePrice(1000.0)
                .currency("USD")
                .payment_currency("USD")
                .saleOfferDate(testDate)
                .salePurchaseStatus(PurchaseStatus.PENDING)
                .financingAgreementDetails(financingDetails)
                .build();

        // Setup sample response model with complete data
        sampleResponseModel = new PurchaseResponseModel();
        sampleResponseModel.setPurchaseOrderId("po123");
        sampleResponseModel.setInventoryId("inv123");
        sampleResponseModel.setFlowerIdentificationNumber("FIN123456");
        sampleResponseModel.setSupplierId("sup123");
        sampleResponseModel.setEmployeeId("emp123");
        sampleResponseModel.setPaymentId("pay123");
        sampleResponseModel.setSalePrice(BigDecimal.valueOf(1000.0));
        sampleResponseModel.setCurrency("USD");
        sampleResponseModel.setSaleOfferDate(testDate);
        sampleResponseModel.setSalePurchaseStatus(PurchaseStatus.PENDING);
        sampleResponseModel.setFinancingAgreementDetails(financingDetails);

        // Set up all the detail objects for complete coverage
        InventoryResponseModel inventoryDetails = new InventoryResponseModel();
        inventoryDetails.setInventoryId("inv123");
        sampleResponseModel.setInventoryDetails(inventoryDetails);

        FlowerResponseModel flowerDetails = new FlowerResponseModel();
        flowerDetails.setFlowerId("FIN123456");
        sampleResponseModel.setFlowerDetails(flowerDetails);

        SupplierResponseModel supplierDetails = new SupplierResponseModel();
        supplierDetails.setSupplierId("sup123");
        sampleResponseModel.setSupplierDetails(supplierDetails);

        PaymentResponseModel paymentDetails = new PaymentResponseModel();
        paymentDetails.setPaymentIdentifier("pay123");
        sampleResponseModel.setPaymentDetails(paymentDetails);

        // Create a list of responses for testing getAllPurchases
        sampleResponseList = new ArrayList<>();
        sampleResponseList.add(sampleResponseModel);

        // Add a second response with different data for more coverage
        PurchaseResponseModel secondResponse = new PurchaseResponseModel();
        secondResponse.setPurchaseOrderId("po456");
        secondResponse.setInventoryId("inv456");
        secondResponse.setFlowerIdentificationNumber("FIN456789");
        secondResponse.setSupplierId("sup456");
        secondResponse.setEmployeeId("emp456");
        secondResponse.setPaymentId("pay456");
        secondResponse.setSalePrice(BigDecimal.valueOf(2000.0));
        secondResponse.setCurrency("EUR");
        secondResponse.setSaleOfferDate(testDate.plusDays(1));
        secondResponse.setSalePurchaseStatus(PurchaseStatus.COMPLETED);

        FinancingAgreementDetails secondFinancing = new FinancingAgreementDetails();
        secondFinancing.setDownPaymentAmount(20000.0);
        secondFinancing.setNumberOfMonthlyPayments(45);
        secondFinancing.setMonthlyPaymentAmount(480.0);
        secondResponse.setFinancingAgreementDetails(secondFinancing);

        sampleResponseList.add(secondResponse);
    }


    @Test
        @DisplayName("Should get all purchases")
        void shouldGetAllPurchases() {
            // Given
            List<PurchaseResponseModel> expectedPurchases = Arrays.asList(sampleResponseModel);
            when(purchaseService.getAllPurchases()).thenReturn(expectedPurchases);

            // When
            List<PurchaseResponseModel> actualPurchases = purchaseController.getAllPurchases();

            // Then
            assertEquals(expectedPurchases, actualPurchases);
            verify(purchaseService).getAllPurchases();
        }

        @Test
        @DisplayName("Should get purchase by ID")
        void shouldGetPurchaseById() {
            // Given
            String purchaseId = "po123";
            when(purchaseService.getPurchaseById(purchaseId)).thenReturn(sampleResponseModel);

            // When
            PurchaseResponseModel actualResponse = purchaseController.getPurchaseById(purchaseId);

            // Then
            assertEquals(sampleResponseModel, actualResponse);
            verify(purchaseService).getPurchaseById(purchaseId);
        }

        @Test
        @DisplayName("Should create purchase")
        void shouldCreatePurchase() {
            // Given
            when(purchaseService.createPurchase(any(PurchaseRequestModel.class))).thenReturn(sampleResponseModel);

            // When
            PurchaseResponseModel actualResponse = purchaseController.createPurchase(sampleRequestModel);

            // Then
            assertEquals(sampleResponseModel, actualResponse);
            verify(purchaseService).createPurchase(sampleRequestModel);
        }

        @Test
        @DisplayName("Should update purchase")
        void shouldUpdatePurchase() {
            // Given
            String purchaseId = "po123";
            when(purchaseService.updatePurchase(eq(purchaseId), any(PurchaseRequestModel.class)))
                    .thenReturn(sampleResponseModel);

            // When
            PurchaseResponseModel actualResponse = purchaseController.updatePurchase(purchaseId, sampleRequestModel);

            // Then
            assertEquals(sampleResponseModel, actualResponse);
            verify(purchaseService).updatePurchase(purchaseId, sampleRequestModel);
        }

        @Test
        @DisplayName("Should delete purchase")
        void shouldDeletePurchase() {
            // Given
            String purchaseId = "po123";
            doNothing().when(purchaseService).deletePurchase(purchaseId);

            // When
            purchaseController.deletePurchase(purchaseId);

            // Then
            verify(purchaseService).deletePurchase(purchaseId);
        }

        @Test
        @DisplayName("Should handle exception when getting purchase by ID")
        void shouldHandleExceptionWhenGettingPurchaseById() {
            // Given
            String invalidPurchaseId = "invalid";
            when(purchaseService.getPurchaseById(invalidPurchaseId))
                    .thenThrow(new RuntimeException("Purchase not found"));

            // When & Then
            Exception exception = assertThrows(RuntimeException.class,
                    () -> purchaseController.getPurchaseById(invalidPurchaseId));
            assertEquals("Purchase not found", exception.getMessage());
        }

    }

//    @Nested
//    @DisplayName("Integration Tests for PurchaseController")
//    @SpringBootTest
//    @AutoConfigureMockMvc
//    class IntegrationTests {
//        @Autowired
//        private MockMvc mockMvc;
//
//        @MockBean
//        private PurchaseServiceImpl purchaseService;
//
//        private ObjectMapper objectMapper = new ObjectMapper();
//        private PurchaseRequestModel sampleRequestModel;
//        private PurchaseResponseModel sampleResponseModel;
//
//        @BeforeEach
//        void setUp() {
//            objectMapper.findAndRegisterModules(); // To handle LocalDate serialization
//            setupTestData();
//        }
//
//        private void setupTestData() {
//            // Setup sample request model
//            FinancingAgreementDetails financingDetails = new FinancingAgreementDetails();
//            financingDetails.setDownPaymentAmount(10000.0);
//            financingDetails.setMonthlyPaymentAmount(50.0);
//            financingDetails.setNumberOfMonthlyPayments(36);
//
//            sampleRequestModel = PurchaseRequestModel.builder()
//                    .inventoryId("inv123")
//                    .flowerIdentificationNumber("FIN123456")
//                    .supplierId("sup123")
//                    .employeeId("emp123")
//                    .paymentId("pay123")
//                    .salePrice(1000.0)
//                    .currency("USD")
//                    .payment_currency("USD")
//                    .saleOfferDate(LocalDate.now())
//                    .salePurchaseStatus(PurchaseStatus.PENDING)
//                    .financingAgreementDetails(financingDetails)
//                    .build();
//
//            // Setup sample response model
//            sampleResponseModel = new PurchaseResponseModel();
//            sampleResponseModel.setPurchaseOrderId("po123");
//            sampleResponseModel.setInventoryId("inv123");
//            sampleResponseModel.setFlowerIdentificationNumber("FIN123456");
//            sampleResponseModel.setSupplierId("sup123");
//            sampleResponseModel.setEmployeeId("emp123");
//            sampleResponseModel.setPaymentId("pay123");
//            sampleResponseModel.setSalePrice(BigDecimal.valueOf(1000.0));
//            sampleResponseModel.setCurrency("USD");
//            sampleResponseModel.setSaleOfferDate(LocalDate.now());
//            sampleResponseModel.setSalePurchaseStatus(PurchaseStatus.PENDING);
//            sampleResponseModel.setFinancingAgreementDetails(financingDetails);
//        }
//
//        @Test
//        @DisplayName("Should return all purchases")
//        void shouldReturnAllPurchases() throws Exception {
//            // Given
//            List<PurchaseResponseModel> expectedPurchases = Arrays.asList(sampleResponseModel);
//            when(purchaseService.getAllPurchases()).thenReturn(expectedPurchases);
//
//            // When & Then
//            mockMvc.perform(get("/api/v1/purchases")
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$", hasSize(1)))
//                    .andExpect(jsonPath("$[0].purchaseOrderId", is("po123")));
//
//            verify(purchaseService).getAllPurchases();
//        }
//
//        @Test
//        @DisplayName("Should return a purchase by ID")
//        void shouldReturnPurchaseById() throws Exception {
//            // Given
//            String purchaseId = "po123";
//            when(purchaseService.getPurchaseById(purchaseId)).thenReturn(sampleResponseModel);
//
//            // When & Then
//            mockMvc.perform(get("/api/v1/purchases/{purchaseId}", purchaseId)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.purchaseOrderId", is("po123")))
//                    .andExpect(jsonPath("$.inventoryId", is("inv123")));
//
//            verify(purchaseService).getPurchaseById(purchaseId);
//        }
//
//        @Test
//        @DisplayName("Should create a purchase")
//        void shouldCreatePurchase() throws Exception {
//            // Given
//            when(purchaseService.createPurchase(any(PurchaseRequestModel.class))).thenReturn(sampleResponseModel);
//
//            // When & Then
//            mockMvc.perform(post("/api/v1/purchases")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(sampleRequestModel))
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.purchaseOrderId", is("po123")));
//
//            verify(purchaseService).createPurchase(any(PurchaseRequestModel.class));
//        }
//
//        @Test
//        @DisplayName("Should update a purchase")
//        void shouldUpdatePurchase() throws Exception {
//            // Given
//            String purchaseId = "po123";
//            when(purchaseService.updatePurchase(eq(purchaseId), any(PurchaseRequestModel.class)))
//                    .thenReturn(sampleResponseModel);
//
//            // When & Then
//            mockMvc.perform(put("/api/v1/purchases/{purchaseId}", purchaseId)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(sampleRequestModel))
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.purchaseOrderId", is("po123")));
//
//            verify(purchaseService).updatePurchase(eq(purchaseId), any(PurchaseRequestModel.class));
//        }
//
//        @Test
//        @DisplayName("Should delete a purchase")
//        void shouldDeletePurchase() throws Exception {
//            // Given
//            String purchaseId = "po123";
//            doNothing().when(purchaseService).deletePurchase(purchaseId);
//
//            // When & Then
//            mockMvc.perform(delete("/api/v1/purchases/{purchaseId}", purchaseId))
//                    .andExpect(status().isOk());
//
//            verify(purchaseService).deletePurchase(purchaseId);
//        }
//
//        @Test
//        @DisplayName("Should handle not found exception when getting purchase by ID")
//        void shouldHandleNotFoundExceptionWhenGettingPurchaseById() throws Exception {
//            // Given
//            String invalidPurchaseId = "invalid";
//            when(purchaseService.getPurchaseById(invalidPurchaseId))
//                    .thenThrow(new RuntimeException("Purchase not found"));
//
//            // When & Then
//            mockMvc.perform(get("/api/v1/purchases/{purchaseId}", invalidPurchaseId)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isInternalServerError());
//
//            verify(purchaseService).getPurchaseById(invalidPurchaseId);
//        }
//
//        @Test
//        @DisplayName("Should handle invalid input when creating purchase")
//        void shouldHandleInvalidInputWhenCreatingPurchase() throws Exception {
//            // Given
//            String invalidJson = "{\"inventoryId\": \"inv123\", invalidJson}";
//
//            // When & Then
//            mockMvc.perform(post("/api/v1/purchases")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(invalidJson)
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isBadRequest());
//
//            verify(purchaseService, never()).createPurchase(any());
//        }
//
//        @Test
//        @DisplayName("Should handle service exception when creating purchase")
//        void shouldHandleServiceExceptionWhenCreatingPurchase() throws Exception {
//            // Given
//            when(purchaseService.createPurchase(any(PurchaseRequestModel.class)))
//                    .thenThrow(new RuntimeException("Failed to create purchase"));
//
//            // When & Then
//            mockMvc.perform(post("/api/v1/purchases")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(sampleRequestModel))
//                            .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isInternalServerError());
//
//            verify(purchaseService).createPurchase(any(PurchaseRequestModel.class));
//        }
//    }