package com.champsoft.services.sales.BusniessLayer;

import com.champsoft.services.sales.BusinessLayer.PurchaseOrderServiceImpl;
import com.champsoft.services.sales.Client.EmployeesServiceClient;
import com.champsoft.services.sales.Client.InventoryServiceClient;
import com.champsoft.services.sales.Client.PaymentsServiceClient;
import com.champsoft.services.sales.Client.SuppliersServiceClient;
import com.champsoft.services.sales.DataLayer.Identifiers.*;
import com.champsoft.services.sales.DataLayer.Purchase.*;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderRequestModelMapper;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderResponseModelMapper;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import com.champsoft.services.sales.PresentationLayer.employeedtos.EmployeeResponseModel;
import com.champsoft.services.sales.PresentationLayer.inventorydtos.FlowerResponseModel;
import com.champsoft.services.sales.PresentationLayer.paymentdtos.PaymentResponseModel;
import com.champsoft.services.sales.PresentationLayer.supplierdtos.SupplierResponseModel;
import com.champsoft.services.sales.utils.Currency;
import com.champsoft.services.sales.PresentationLayer.inventorydtos.InventoryResponseModel;
import com.champsoft.services.sales.utils.LowSalePriceException;
import com.champsoft.services.sales.utils.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PurchaseServiceImplTest2 {

    @Mock private PurchaseOrderRepository repository;
    @Mock private PurchaseOrderRequestModelMapper requestMapper;
    @Mock private PurchaseOrderResponseModelMapper responseMapper;
    @Mock private SuppliersServiceClient suppliersClient;
    @Mock private EmployeesServiceClient employeesClient;
    @Mock private InventoryServiceClient inventoryClient;
    @Mock private PaymentsServiceClient paymentsServiceClient;
    @InjectMocks private PurchaseOrderServiceImpl service;

    private PurchaseOrder entity;
    private PurchaseRequestModel request;
    private PurchaseResponseModel response;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        request = PurchaseRequestModel.builder()
                .purchaseOrderId("p123")
                .supplierId("sup-123")
                .employeeId("emp-456")
                .inventoryId("inv-789")
                .flowerIdentificationNumber("fl-001")
                .salePrice(100.0)
                .currency("USD")
                .saleOfferDate(LocalDate.now())
                .salePurchaseStatus(PurchaseStatus.PENDING)
                .build();

        entity = new PurchaseOrder();
        entity.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("p123"));

        response = new PurchaseResponseModel();
        response.setPurchaseOrderId("p123");
    }

    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(responseMapper.entityToResponseModel(entity)).thenReturn(response);

        List<PurchaseResponseModel> result = service.getAllPurchaseOrders();

        assertEquals(1, result.size());
        assertEquals("p123", result.get(0).getPurchaseOrderId());
    }

    @Test
    void testGetById_found() {
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(response);

        PurchaseResponseModel result = service.getPurchaseOrderById("p123");
        assertEquals("p123", result.getPurchaseOrderId());
    }

    @Test
    void testGetById_notFound() {
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.getPurchaseOrderById("p123"));
    }



    @Test
    void testAdd_missingSupplier() {
        when(suppliersClient.getSupplierBySupplierId("sup-123"))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.addPurchaseOrder(request));
    }



    @Test
    void testUpdate_notFound() {
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.updatePurchaseOrder("p123", request));
    }

    @Test
    void testDelete_valid() {
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(entity);

        assertDoesNotThrow(() -> service.deletePurchaseOrder("p123"));
        verify(repository, times(1)).delete(entity);
    }

    @Test
    void testDelete_notFound() {
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.deletePurchaseOrder("p123"));
    }
    @Test
    void testAdd_salePriceTooLow() {
        request.setSalePrice(10.0); // Too low

        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getInventoryById("inv-789")).thenReturn(new InventoryResponseModel());
        when(inventoryClient.getFlowerByFlowerId(anyString(), anyString()))
                .thenReturn(new FlowerResponseModel());

        assertThrows(LowSalePriceException.class, () -> service.addPurchaseOrder(request));
    }

    @Test
    void testAdd_flowerNotFound() {
        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getInventoryById("inv-789")).thenReturn(new InventoryResponseModel());
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.addPurchaseOrder(request));
    }

    @Test
    void testAdd_inventoryNotFound() {
        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getInventoryById("inv-789")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.addPurchaseOrder(request));
    }

    @Test
    void testAdd_invalidPaymentId() {
        request.setPaymentId("pay-001");

        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getInventoryById("inv-789")).thenReturn(new InventoryResponseModel());
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001")).thenReturn(new FlowerResponseModel());
        when(paymentsServiceClient.getPaymentById("pay-001")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.addPurchaseOrder(request));
    }

    @Test
    void testAdd_happyPath() {
        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getInventoryById("inv-789")).thenReturn(new InventoryResponseModel());
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001")).thenReturn(new FlowerResponseModel());
        when(requestMapper.requestModelToEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(responseMapper.entityToResponseModel(any())).thenReturn(response);

        PurchaseResponseModel result = service.addPurchaseOrder(request);
        assertNotNull(result);
        assertEquals("p123", result.getPurchaseOrderId());
    }

    @Test
    void testUpdate_successfulUpdate() {
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(responseMapper.entityToResponseModel(any())).thenReturn(response);

        // ✅ External service mocks
        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getInventoryById("inv-789")).thenReturn(new InventoryResponseModel());
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001")).thenReturn(new FlowerResponseModel());

        PurchaseResponseModel result = service.updatePurchaseOrder("p123", request);

        assertNotNull(result);
        assertEquals("p123", result.getPurchaseOrderId());
    }
    @Test
    void testGetAllPurchaseOrders_enrichmentSuccessAndFailure() {
        PurchaseResponseModel enrichedResponse = new PurchaseResponseModel();
        enrichedResponse.setPurchaseOrderId("p123");
        enrichedResponse.setPaymentId("pay-001");
        enrichedResponse.setInventoryId("inv-789");
        enrichedResponse.setFlowerIdentificationNumber("fl-001");
        enrichedResponse.setSupplierId("sup-123");
        enrichedResponse.setEmployeeId("emp-456");

        when(repository.findAll()).thenReturn(List.of(entity));
        when(responseMapper.entityToResponseModel(entity)).thenReturn(enrichedResponse);

        // Replace 'new Object()' with proper mocks of return types
        when(paymentsServiceClient.getPaymentById("pay-001"))
                .thenReturn(mock(com.champsoft.services.sales.PresentationLayer.paymentdtos.PaymentResponseModel.class));
        when(inventoryClient.getInventoryById("inv-789"))
                .thenReturn(mock(InventoryResponseModel.class));
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001"))
                .thenReturn(mock(FlowerResponseModel.class));
        when(suppliersClient.getSupplierBySupplierId("sup-123"))
                .thenReturn(mock(SupplierResponseModel.class));
        when(employeesClient.getEmployeeByEmployeeId("emp-456"))
                .thenReturn(mock(EmployeeResponseModel.class));

        List<PurchaseResponseModel> results = service.getAllPurchaseOrders();
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getPaymentDetails());
        assertNotNull(results.get(0).getInventoryDetails());
        assertNotNull(results.get(0).getFlowerDetails());
        assertNotNull(results.get(0).getSupplierDetails());
        assertNotNull(results.get(0).getEmployeeDetails());

        // Simulate exceptions in external clients
        when(paymentsServiceClient.getPaymentById("pay-001")).thenThrow(new RuntimeException("fail payment"));
        when(inventoryClient.getInventoryById("inv-789")).thenThrow(new RuntimeException("fail inventory"));
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001")).thenThrow(new RuntimeException("fail flower"));
        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenThrow(new RuntimeException("fail supplier"));
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenThrow(new RuntimeException("fail employee"));

        results = service.getAllPurchaseOrders();
        assertEquals(1, results.size());
        assertNull(results.get(0).getPaymentDetails());
        assertNull(results.get(0).getInventoryDetails());
        assertNull(results.get(0).getFlowerDetails());
        assertNull(results.get(0).getSupplierDetails());
        assertNull(results.get(0).getEmployeeDetails());
    }


    @Test
    void testGetPurchaseOrderById_enrichmentWithExceptions() {
        PurchaseResponseModel enrichedResponse = new PurchaseResponseModel();
        enrichedResponse.setPurchaseOrderId("p123");
        enrichedResponse.setPaymentId("pay-001");
        enrichedResponse.setInventoryId("inv-789");
        enrichedResponse.setFlowerIdentificationNumber("fl-001");
        enrichedResponse.setSupplierId("sup-123");
        enrichedResponse.setEmployeeId("emp-456");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(enrichedResponse);

        // Use proper mocks instead of new Object()
        when(paymentsServiceClient.getPaymentById("pay-001"))
                .thenReturn(mock(com.champsoft.services.sales.PresentationLayer.paymentdtos.PaymentResponseModel.class));
        when(inventoryClient.getInventoryById("inv-789"))
                .thenReturn(mock(InventoryResponseModel.class));
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001"))
                .thenReturn(mock(FlowerResponseModel.class));
        when(suppliersClient.getSupplierBySupplierId("sup-123"))
                .thenReturn(mock(SupplierResponseModel.class));
        when(employeesClient.getEmployeeByEmployeeId("emp-456"))
                .thenReturn(mock(EmployeeResponseModel.class));

        PurchaseResponseModel result = service.getPurchaseOrderById("p123");
        assertNotNull(result.getPaymentDetails());
        assertNotNull(result.getInventoryDetails());
        assertNotNull(result.getFlowerDetails());
        assertNotNull(result.getSupplierDetails());
        assertNotNull(result.getEmployeeDetails());

        // Simulate exceptions during enrichment
        when(paymentsServiceClient.getPaymentById("pay-001")).thenThrow(new RuntimeException());
        when(inventoryClient.getInventoryById("inv-789")).thenThrow(new RuntimeException());
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001")).thenThrow(new RuntimeException());
        when(suppliersClient.getSupplierBySupplierId("sup-123")).thenThrow(new RuntimeException());
        when(employeesClient.getEmployeeByEmployeeId("emp-456")).thenThrow(new RuntimeException());

        result = service.getPurchaseOrderById("p123");
        assertNull(result.getPaymentDetails());
        assertNull(result.getInventoryDetails());
        assertNull(result.getFlowerDetails());
        assertNull(result.getSupplierDetails());
        assertNull(result.getEmployeeDetails());
    }

//    @Test
//    void testUpdateFinancingAgreementDetails_whenFinancingDetailsPresent_updatesFieldsAndPersists() {
//        // Arrange
//        PurchaseRequestModel requestModel = mock(PurchaseRequestModel.class);
//        FinancingAgreementDetails requestFinancing = new FinancingAgreementDetails(12, 100.0, 500.0);
//        when(requestModel.getFinancingAgreementDetails()).thenReturn(requestFinancing);
//
//        PurchaseOrder existingOrder = PurchaseOrder.builder()
//                .purchaseOrderIdentifier(new PurchaseOrderIdentifier("p123"))
//                .financingAgreementDetails(new FinancingAgreementDetails())
//                .build();
//
//        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
//                .thenReturn(existingOrder);
//        when(repository.save(any(PurchaseOrder.class))).thenReturn(existingOrder);
//
//        // Act
//        service.updatePurchaseOrder("p123", requestModel);
//
//        // Assert on the existingOrder (internal state change)
//        assertEquals(12, existingOrder.getFinancingAgreementDetails().getNumberOfMonthlyPayments());
//        assertEquals(100.0, existingOrder.getFinancingAgreementDetails().getMonthlyPaymentAmount());
//        assertEquals(500.0, existingOrder.getFinancingAgreementDetails().getDownPaymentAmount());
//
//        ArgumentCaptor<PurchaseOrder> purchaseOrderCaptor = ArgumentCaptor.forClass(PurchaseOrder.class);
//        verify(repository, times(1)).save(purchaseOrderCaptor.capture());
//        PurchaseOrder savedOrder = purchaseOrderCaptor.getValue();
//
//        assertNotNull(savedOrder.getFinancingAgreementDetails());
//        assertEquals(12, savedOrder.getFinancingAgreementDetails().getNumberOfMonthlyPayments());
//        assertEquals(100.0, savedOrder.getFinancingAgreementDetails().getMonthlyPaymentAmount());
//        assertEquals(500.0, savedOrder.getFinancingAgreementDetails().getDownPaymentAmount());
//    }

    @Test
    void testEnrichPurchaseResponseModel_setsPaymentDetails_whenPaymentIdExists() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setPaymentIdentifier(new PaymentIdentifier("payment-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setPaymentId("payment-123");

        PaymentResponseModel paymentDetails = new PaymentResponseModel();
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(paymentsServiceClient.getPaymentById("payment-123")).thenReturn(paymentDetails);

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertEquals(paymentDetails, result.getPaymentDetails());
    }



    @Test
    void testEnrichPurchaseResponseModel_setsPaymentDetailsToNull_whenExceptionThrown() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setPaymentIdentifier(new PaymentIdentifier("payment-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setPaymentId("payment-123");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(paymentsServiceClient.getPaymentById("payment-123")).thenThrow(new RuntimeException("fail"));

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertNull(result.getPaymentDetails());
    }

    @Test
    void testEnrichPurchaseResponseModel_setsInventoryDetails_whenInventoryIdExists() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setInventoryIdentifier(new InventoryIdentifier("inv-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setInventoryId("inv-123");

        InventoryResponseModel inventoryDetails = new InventoryResponseModel();
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(inventoryClient.getInventoryById("inv-123")).thenReturn(inventoryDetails);

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertEquals(inventoryDetails, result.getInventoryDetails());
    }

    @Test
    void testEnrichPurchaseResponseModel_setsInventoryDetailsToNull_whenExceptionThrown() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setInventoryIdentifier(new InventoryIdentifier("inv-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setInventoryId("inv-123");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(inventoryClient.getInventoryById("inv-123")).thenThrow(new RuntimeException("fail"));

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertNull(result.getInventoryDetails());
    }



    @Test
    void testEnrichPurchaseResponseModel_setsFlowerDetails_whenInventoryIdAndFlowerIdExist() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setInventoryIdentifier(new InventoryIdentifier("inv-123"));
        entity.setFlowerIdentifier(new FlowerIdentifier("flower-456"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setInventoryId("inv-123");
        responseWithoutDetails.setFlowerIdentificationNumber("flower-456");

        FlowerResponseModel flowerDetails = new FlowerResponseModel();
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(inventoryClient.getFlowerByFlowerId("inv-123", "flower-456")).thenReturn(flowerDetails);

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertEquals(flowerDetails, result.getFlowerDetails());
    }

    @Test
    void testEnrichPurchaseResponseModel_setsFlowerDetailsToNull_whenExceptionThrown() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setInventoryIdentifier(new InventoryIdentifier("inv-123"));
        entity.setFlowerIdentifier(new FlowerIdentifier("flower-456"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setInventoryId("inv-123");
        responseWithoutDetails.setFlowerIdentificationNumber("flower-456");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(inventoryClient.getFlowerByFlowerId("inv-123", "flower-456")).thenThrow(new RuntimeException("fail"));

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertNull(result.getFlowerDetails());
    }

// For supplierDetails

    @Test
    void testEnrichPurchaseResponseModel_setsSupplierDetails_whenSupplierIdExists() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setSupplierIdentifier(new SupplierIdentifier("supplier-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setSupplierId("supplier-123");

        SupplierResponseModel supplierDetails = new SupplierResponseModel();
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(suppliersClient.getSupplierBySupplierId("supplier-123")).thenReturn(supplierDetails);

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertEquals(supplierDetails, result.getSupplierDetails());
    }

    @Test
    void testEnrichPurchaseResponseModel_setsSupplierDetailsToNull_whenExceptionThrown() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setSupplierIdentifier(new SupplierIdentifier("supplier-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setSupplierId("supplier-123");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(suppliersClient.getSupplierBySupplierId("supplier-123")).thenThrow(new RuntimeException("fail"));

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertNull(result.getSupplierDetails());
    }

// For employeeDetails

    @Test
    void testEnrichPurchaseResponseModel_setsEmployeeDetails_whenEmployeeIdExists() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setEmployeeIdentifier(new EmployeeIdentifier("emp-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setEmployeeId("emp-123");

        EmployeeResponseModel employeeDetails = new EmployeeResponseModel();
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(employeesClient.getEmployeeByEmployeeId("emp-123")).thenReturn(employeeDetails);

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertEquals(employeeDetails, result.getEmployeeDetails());
    }

    @Test
    void testEnrichPurchaseResponseModel_setsEmployeeDetailsToNull_whenExceptionThrown() throws Exception {
        // Arrange
        PurchaseOrder entity = new PurchaseOrder();
        entity.getPurchaseOrderIdentifier().setPurchaseId("po-123");
        entity.setEmployeeIdentifier(new EmployeeIdentifier("emp-123"));

        PurchaseResponseModel responseWithoutDetails = new PurchaseResponseModel();
        responseWithoutDetails.setPurchaseOrderId("po-123");
        responseWithoutDetails.setEmployeeId("emp-123");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("po-123")).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(responseWithoutDetails);
        when(employeesClient.getEmployeeByEmployeeId("emp-123")).thenThrow(new RuntimeException("fail"));

        // Act
        PurchaseResponseModel result = service.getPurchaseOrderById("po-123");

        // Assert
        assertNull(result.getEmployeeDetails());
    }
}