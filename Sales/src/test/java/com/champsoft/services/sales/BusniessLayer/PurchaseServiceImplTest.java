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


        @Mock PurchaseOrderRepository purchaseOrderRepository;
        @Mock PurchaseOrderRequestModelMapper requestModelMapper;
        @Mock PurchaseOrderResponseModelMapper responseModelMapper;
        @Mock SuppliersServiceClient suppliersServiceClient;
        @Mock EmployeesServiceClient employeesServiceClient;
        @Mock InventoryServiceClient inventoryServiceClient;



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
            when(purchaseOrderRepository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("missing")).thenReturn(null);

            NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getPurchaseOrderById("missing"));
            assertTrue(ex.getMessage().contains("not found"));
        }

        @Test
        void deletePurchaseOrder_notFound_throwsNotFound() {
            when(purchaseOrderRepository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("missing")).thenReturn(null);

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

}

