package com.champsoft.services.sales.BusniessLayer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.champsoft.services.sales.DataLayer.Identifiers.EmployeeIdentifier;
import com.champsoft.services.sales.DataLayer.Identifiers.InventoryIdentifier;
import com.champsoft.services.sales.DataLayer.Identifiers.SupplierIdentifier;
import com.champsoft.services.sales.PresentationLayer.*; // For models
   // For domain classes

import com.champsoft.services.sales.BusinessLayer.PurchaseOrderServiceImpl;
import com.champsoft.services.sales.Client.EmployeesServiceClient;
import com.champsoft.services.sales.Client.InventoryServiceClient;
import com.champsoft.services.sales.Client.PaymentsServiceClient;
import com.champsoft.services.sales.Client.SuppliersServiceClient;
import com.champsoft.services.sales.DataLayer.Purchase.PurchaseOrder;
import com.champsoft.services.sales.DataLayer.Purchase.PurchaseOrderIdentifier;
import com.champsoft.services.sales.DataLayer.Purchase.PurchaseOrderRepository;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderRequestModelMapper;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderResponseModelMapper;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import com.champsoft.services.sales.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceImplTest {

    @Mock
    SuppliersServiceClient suppliersServiceClient;

    @Mock
    EmployeesServiceClient employeesServiceClient;

    @Mock
    InventoryServiceClient inventoryServiceClient;

    @Mock
    PaymentsServiceClient paymentsServiceClient;
    @Mock
    PurchaseOrderRepository purchaseOrderRepository;
    @Mock
    PurchaseOrderRequestModelMapper requestModelMapper;
    @Mock
    PurchaseOrderResponseModelMapper responseModelMapper;
    @InjectMocks
    PurchaseOrderServiceImpl purchaseOrderService;
    private PurchaseRequestModel sampleRequest;
    private PurchaseOrder sampleEntity;
    private PurchaseResponseModel sampleResponse;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        purchaseOrderService = new PurchaseOrderServiceImpl(
                purchaseOrderRepository,
                requestModelMapper,
                responseModelMapper,
                suppliersServiceClient,
                employeesServiceClient,
                inventoryServiceClient,
                paymentsServiceClient
        );
    }

//    @Test
//    void addPurchaseOrder_ShouldThrowNotFound_WhenSupplierMissing() {
//        when(suppliersServiceClient.getSupplierBySupplierId("S1")).thenReturn(null);
//        assertThrows(NotFoundException.class, () -> purchaseOrderService.addPurchaseOrder(sampleRequest));
//    }
//    @Test
//    void enrichPurchaseResponseModel_ShouldSetNull_WhenExternalServiceFails() {
//        sampleResponse.setPaymentId("FAIL");
//        when(paymentsServiceClient.getPaymentById("FAIL")).thenThrow(new RuntimeException("Error"));
//
//        when(responseModelMapper.entityToResponseModel(any())).thenReturn(sampleResponse);
//        when(purchaseOrderRepository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("P1")).thenReturn(sampleEntity);
//
//        PurchaseResponseModel result = purchaseOrderService.getPurchaseOrderById("P1");
//        assertNull(result.getPaymentDetails());
//    }


    @Test
    void enrichPurchaseResponseModel_returnsNullIfInputIsNull() throws Exception {
        java.lang.reflect.Method method = PurchaseOrderServiceImpl.class.getDeclaredMethod("enrichPurchaseResponseModel", PurchaseResponseModel.class);
        method.setAccessible(true);

        PurchaseResponseModel result = (PurchaseResponseModel) method.invoke(purchaseOrderService, (PurchaseResponseModel) null);

        assertNull(result);
    }

    @Test
    void enrichPurchaseResponseModel_noIds_noEnrichment() throws Exception {
        PurchaseResponseModel response = new PurchaseResponseModel();
        // all IDs null by default

        java.lang.reflect.Method method = PurchaseOrderServiceImpl.class.getDeclaredMethod("enrichPurchaseResponseModel", PurchaseResponseModel.class);
        method.setAccessible(true);

        PurchaseResponseModel result = (PurchaseResponseModel) method.invoke(purchaseOrderService, response);

        assertNotNull(result);
        verifyNoInteractions(paymentsServiceClient, inventoryServiceClient, suppliersServiceClient, employeesServiceClient);
    }


    @Test
    void enrichPurchaseResponseModel_handlesExceptions() throws Exception {
        PurchaseResponseModel response = new PurchaseResponseModel();
        response.setPaymentId("payment123");
        response.setInventoryId("inventory123");
        response.setFlowerIdentificationNumber("flower123");
        response.setSupplierId("supplier123");
        response.setEmployeeId("employee123");

        // Setup mocks to throw exceptions
        when(paymentsServiceClient.getPaymentById(anyString())).thenThrow(new RuntimeException("fail payment"));
        when(inventoryServiceClient.getInventoryById(anyString())).thenThrow(new RuntimeException("fail inventory"));
        when(inventoryServiceClient.getFlowerByFlowerId(anyString(), anyString())).thenThrow(new RuntimeException("fail flower"));
        when(suppliersServiceClient.getSupplierBySupplierId(anyString())).thenThrow(new RuntimeException("fail supplier"));
        when(employeesServiceClient.getEmployeeByEmployeeId(anyString())).thenThrow(new RuntimeException("fail employee"));

        // Use reflection to access the private method
        java.lang.reflect.Method method = PurchaseOrderServiceImpl.class.getDeclaredMethod("enrichPurchaseResponseModel", PurchaseResponseModel.class);
        method.setAccessible(true);

        PurchaseResponseModel result = (PurchaseResponseModel) method.invoke(purchaseOrderService, response);

        assertNotNull(result);
        assertNull(result.getPaymentDetails());
        assertNull(result.getInventoryDetails());
        assertNull(result.getFlowerDetails());
        assertNull(result.getSupplierDetails());
        assertNull(result.getEmployeeDetails());
    }
//    @Test
//    void addPurchaseOrder_ShouldThrowNotFound_WhenFlowerMissing() {
//        when(suppliersServiceClient.getSupplierBySupplierId("S1")).thenReturn(new SupplierIdentifier("S1"));
//        when(employeesServiceClient.getEmployeeByEmployeeId("E1")).thenReturn(new EmployeeIdentifier("E1"));
//        when(inventoryServiceClient.getInventoryById("I1")).thenReturn(new InventoryIdentifier("I1"));
//        when(inventoryServiceClient.getFlowerByFlowerId("I1", "F1")).thenReturn(null);
//
//        assertThrows(NotFoundException.class, () -> purchaseOrderService.addPurchaseOrder(sampleRequest));
//    }


//    @Test
//    void addPurchaseOrder_ShouldThrowNotFound_WhenPaymentInvalid() {
//        sampleRequest.setPaymentId("PAY1");
//        when(suppliersServiceClient.getSupplierBySupplierId("S1")).thenReturn(new FakeSupplier());
//        when(employeesServiceClient.getEmployeeByEmployeeId("E1")).thenReturn(new FakeEmployee());
//        when(inventoryServiceClient.getInventoryById("I1")).thenReturn(new FakeInventory());
//        when(inventoryServiceClient.getFlowerByFlowerId("I1", "F1")).thenReturn(new FakeFlower());
//        when(paymentsServiceClient.getPaymentById("PAY1")).thenReturn(null);
//
//        assertThrows(NotFoundException.class, () -> purchaseOrderService.addPurchaseOrder(sampleRequest));
//    }
//
//    @Test
//    void addPurchaseOrder_ShouldReturnResponse_WhenAllValid() {
//        when(suppliersServiceClient.getSupplierBySupplierId("S1")).thenReturn(new FakeSupplier());
//        when(employeesServiceClient.getEmployeeByEmployeeId("E1")).thenReturn(new FakeEmployee());
//        when(inventoryServiceClient.getInventoryById("I1")).thenReturn(new FakeInventory());
//        when(inventoryServiceClient.getFlowerByFlowerId("I1", "F1")).thenReturn(new FakeFlower());
//        when(paymentsServiceClient.getPaymentById("PAY1")).thenReturn(new FakePayment());
//
//        when(requestModelMapper.requestModelToEntity(sampleRequest)).thenReturn(sampleEntity);
//        when(purchaseOrderRepository.save(any())).thenReturn(sampleEntity);
//        when(responseModelMapper.entityToResponseModel(sampleEntity)).thenReturn(sampleResponse);
//
//        PurchaseResponseModel result = purchaseOrderService.addPurchaseOrder(sampleRequest);
//
//        assertNotNull(result);
//        verify(purchaseOrderRepository).save(any());
//    }
//
//    @Test
//    void addPurchaseOrder_ShouldThrowLowSalePriceException_WhenPriceTooLow() {
//        sampleRequest.setSalePrice(BigDecimal.valueOf(10).doubleValue());
//        when(suppliersServiceClient.getSupplierBySupplierId("S1")).thenReturn(new FakeSupplier());
//        when(employeesServiceClient.getEmployeeByEmployeeId("E1")).thenReturn(new FakeEmployee());
//        when(inventoryServiceClient.getInventoryById("I1")).thenReturn(new FakeInventory());
//        when(inventoryServiceClient.getFlowerByFlowerId("I1", "F1")).thenReturn(new FakeFlower());
//
//        assertThrows(LowSalePriceException.class, () -> purchaseOrderService.addPurchaseOrder(sampleRequest));
//    }
//
//    @Test
//    void addPurchaseOrder_ShouldSetDefaults_WhenMissingPriceAndStatus() {
//        sampleRequest.setSalePrice(null);
//        when(suppliersServiceClient.getSupplierBySupplierId("S1")).thenReturn(new FakeSupplier());
//        when(employeesServiceClient.getEmployeeByEmployeeId("E1")).thenReturn(new FakeEmployee());
//        when(inventoryServiceClient.getInventoryById("I1")).thenReturn(new FakeInventory());
//        when(inventoryServiceClient.getFlowerByFlowerId("I1", "F1")).thenReturn(new FakeFlower());
//
//        when(requestModelMapper.requestModelToEntity(any())).thenReturn(new PurchaseOrder());
//        when(purchaseOrderRepository.save(any())).thenReturn(sampleEntity);
//        when(responseModelMapper.entityToResponseModel(any())).thenReturn(sampleResponse);
//
//        PurchaseResponseModel result = purchaseOrderService.addPurchaseOrder(sampleRequest);
//
//        assertNotNull(result);
//        verify(purchaseOrderRepository).save(any());
//    }

}
