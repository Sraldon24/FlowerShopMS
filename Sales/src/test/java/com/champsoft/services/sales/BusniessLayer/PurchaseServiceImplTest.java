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

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PurchaseServiceImplTest {

    @Mock private PurchaseOrderRepository repository;
    @Mock private PurchaseOrderRequestModelMapper requestMapper;
    @Mock private PurchaseOrderResponseModelMapper responseMapper;
    @Mock private PaymentsServiceClient paymentsServiceClient;
    @Mock SuppliersServiceClient suppliersServiceClient;
    @Mock EmployeesServiceClient employeesServiceClient;
    @Mock InventoryServiceClient inventoryServiceClient;



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
        void addPurchaseOrder_unknownSupplier_throwsNotFound() {
            PurchaseRequestModel req = new PurchaseRequestModel();
            req.setSupplierId("badSupplier");
            when(suppliersServiceClient.getSupplierBySupplierId("badSupplier")).thenReturn(null);

            NotFoundException ex = assertThrows(NotFoundException.class, () -> service.addPurchaseOrder(req));
            assertTrue(ex.getMessage().contains("Unknown supplier ID"));
        }



//        @Test
//        void updatePurchaseOrder_lowSalePrice_throwsLowSalePrice() {
//            String purchaseId = "p1";
//            PurchaseRequestModel req = new PurchaseRequestModel();
//            req.setSalePrice(BigDecimal.valueOf(10).doubleValue());
//            req.setCurrency("USD");
//
//            PurchaseOrder existing = new PurchaseOrder();
//            when(purchaseOrderRepository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId)).thenReturn(existing);
//
//            LowSalePriceException ex = assertThrows(LowSalePriceException.class, () -> service.updatePurchaseOrder(purchaseId, req));
//            assertTrue(ex.getMessage().contains("Sale price must be at least 20.00"));
//        }

        @Test
        void getPurchaseOrderById_notFound_throwsNotFound() {
            when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("missing")).thenReturn(null);

            NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getPurchaseOrderById("missing"));
            assertTrue(ex.getMessage().contains("not found"));
        }

        @Test
        void deletePurchaseOrder_notFound_throwsNotFound() {
            when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("missing")).thenReturn(null);

            NotFoundException ex = assertThrows(NotFoundException.class, () -> service.deletePurchaseOrder("missing"));
            assertTrue(ex.getMessage().contains("No purchase order found"));
        }

        @Test
        void enrichPurchaseResponseModel_handlesServiceClientExceptions() {
            PurchaseResponseModel response = new PurchaseResponseModel();
            response.setPaymentId("pay1");
            response.setInventoryId("inv1");
            response.setFlowerIdentificationNumber("flower1");
            response.setSupplierId("sup1");
            response.setEmployeeId("emp1");

            when(paymentsServiceClient.getPaymentById(any())).thenThrow(new RuntimeException("fail payment"));
            when(inventoryServiceClient.getInventoryById(any())).thenThrow(new RuntimeException("fail inventory"));
            when(inventoryServiceClient.getFlowerByFlowerId(any(), any())).thenThrow(new RuntimeException("fail flower"));
            when(suppliersServiceClient.getSupplierBySupplierId(any())).thenThrow(new RuntimeException("fail supplier"));
            when(employeesServiceClient.getEmployeeByEmployeeId(any())).thenThrow(new RuntimeException("fail employee"));

            try {
                PurchaseResponseModel enriched = (PurchaseResponseModel) PurchaseOrderServiceImpl.class
                        .getDeclaredMethod("enrichPurchaseResponseModel", PurchaseResponseModel.class)
                        .invoke(service, response);
                assertNull(enriched.getPaymentDetails());
                assertNull(enriched.getInventoryDetails());
                assertNull(enriched.getFlowerDetails());
                assertNull(enriched.getSupplierDetails());
                assertNull(enriched.getEmployeeDetails());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

                e.printStackTrace();
            }

        }
//    @Test
//    void updatePurchaseOrder_notFound_throwsNotFound() {
//        String purchaseId = "nonexistent";
//        PurchaseRequestModel request = new PurchaseRequestModel();
//
//        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId)).thenReturn(null);
//
//        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.updatePurchaseOrder(purchaseId, request));
//        assertTrue(ex.getMessage().contains("not found"));
//    }

    @Test
    void updatePurchaseOrder_lowPrice_throwsLowSalePrice() {
        String purchaseId = "p1";
        PurchaseOrder existing = new PurchaseOrder();
        PurchaseRequestModel request = new PurchaseRequestModel();
        request.setSalePrice(10.0); // too low
        request.setCurrency("USD");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId)).thenReturn(existing);

        LowSalePriceException ex = assertThrows(LowSalePriceException.class, () -> service.updatePurchaseOrder(purchaseId, request));
        assertTrue(ex.getMessage().contains("Sale price must be at least"));
    }

    @Test
    void updatePurchaseOrder_defaultsAreSet_whenMissingFields() {
        String purchaseId = "p1";
        PurchaseOrder existing = new PurchaseOrder();
        PurchaseRequestModel request = new PurchaseRequestModel();
        request.setSalePrice(null);
        request.setCurrency(null);

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId)).thenReturn(existing);
        when(repository.save(any())).thenReturn(existing);
        when(responseMapper.entityToResponseModel(any())).thenReturn(new PurchaseResponseModel());

        PurchaseResponseModel result = service.updatePurchaseOrder(purchaseId, request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void updatePurchaseOrder_allValid_returnsResponse() {
        String purchaseId = "p1";
        PurchaseOrder existing = new PurchaseOrder();
        PurchaseRequestModel request = new PurchaseRequestModel();
        request.setSalePrice(100.0);
        request.setCurrency("USD");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId)).thenReturn(existing);
        when(repository.save(any())).thenReturn(existing);
        when(responseMapper.entityToResponseModel(any())).thenReturn(new PurchaseResponseModel());

        PurchaseResponseModel result = service.updatePurchaseOrder(purchaseId, request);

        assertNotNull(result);
        verify(repository).save(any());
    }


    private final PurchaseOrderResponseModelMapper mapper = new PurchaseOrderResponseModelMapper();

    @Test
    void entityToResponseModelList_validList_returnsMappedList() {
        PurchaseOrder order1 = new PurchaseOrder();
        order1.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("p1"));

        PurchaseOrder order2 = new PurchaseOrder();
        order2.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("p2"));

        List<PurchaseOrder> orders = List.of(order1, order2);

        List<PurchaseResponseModel> result = mapper.entityToResponseModelList(orders);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("p1", result.get(0).getPurchaseOrderId());
        assertEquals("p2", result.get(1).getPurchaseOrderId());
    }

    @Test
    void updatePurchaseOrder_unknownPaymentId_throwsNotFound() {
        String purchaseId = "p1";
        PurchaseOrder existing = new PurchaseOrder();
        PurchaseRequestModel request = new PurchaseRequestModel();
        request.setPaymentId("invalid-payment-id");

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId)).thenReturn(existing);
        when(paymentsServiceClient.getPaymentById("invalid-payment-id")).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.updatePurchaseOrder(purchaseId, request));
        assertTrue(ex.getMessage().contains("Unknown payment ID"));
    }
}

