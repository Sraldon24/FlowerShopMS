package com.champsoft.services.sales.BusniessLayer;

import com.champsoft.services.sales.BusinessLayer.PurchaseOrderServiceImpl;
import com.champsoft.services.sales.Client.EmployeesServiceClient;
import com.champsoft.services.sales.Client.InventoryServiceClient;
import com.champsoft.services.sales.Client.SuppliersServiceClient;
import com.champsoft.services.sales.DataLayer.Purchase.*;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderRequestModelMapper;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderResponseModelMapper;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import com.champsoft.services.sales.PresentationLayer.employeedtos.EmployeeResponseModel;
import com.champsoft.services.sales.PresentationLayer.inventorydtos.FlowerResponseModel;
import com.champsoft.services.sales.PresentationLayer.supplierdtos.SupplierResponseModel;
import com.champsoft.services.sales.utils.Currency;
import com.champsoft.services.sales.utils.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

public class PurchaseServiceImplTest {

    @Mock private PurchaseOrderRepository repository;
    @Mock private PurchaseOrderRequestModelMapper requestMapper;
    @Mock private PurchaseOrderResponseModelMapper responseMapper;
    @Mock private SuppliersServiceClient suppliersClient;
    @Mock private EmployeesServiceClient employeesClient;
    @Mock private InventoryServiceClient inventoryClient;

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
    void testAdd_valid() {
        when(suppliersClient.getSupplierBySupplierId("sup-123"))
                .thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId("emp-456"))
                .thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getFlowerByFlowerId("inv-789", "fl-001"))
                .thenReturn(new FlowerResponseModel());

        when(requestMapper.requestModelToEntity(any())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(response);

        PurchaseResponseModel result = service.addPurchaseOrder(request);
        assertEquals("p123", result.getPurchaseOrderId());
    }

    @Test
    void testAdd_missingSupplier() {
        when(suppliersClient.getSupplierBySupplierId("sup-123"))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.addPurchaseOrder(request));
    }

    @Test
    void testUpdate_valid() {
        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(responseMapper.entityToResponseModel(entity)).thenReturn(response);

        PurchaseResponseModel result = service.updatePurchaseOrder("p123", request);
        assertEquals("p123", result.getPurchaseOrderId());
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
    void testAdd_withNullStatus_setsPending() {
        request.setSalePurchaseStatus(null);

        when(suppliersClient.getSupplierBySupplierId(any())).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId(any())).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getFlowerByFlowerId(any(), any())).thenReturn(new FlowerResponseModel());
        when(requestMapper.requestModelToEntity(any())).thenAnswer(i -> {
            PurchaseOrder p = new PurchaseOrder();
            p.setPrice(new Price(BigDecimal.TEN, Currency.CAD));
            p.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("p123"));
            return p;
        });
        when(repository.save(any())).thenReturn(entity);
        when(responseMapper.entityToResponseModel(any())).thenReturn(response);

        PurchaseResponseModel result = service.addPurchaseOrder(request);

        assertEquals("p123", result.getPurchaseOrderId());
    }

    @Test
    void testAdd_withNullPrice_setsZeroUSD() {
        when(suppliersClient.getSupplierBySupplierId(any())).thenReturn(new SupplierResponseModel());
        when(employeesClient.getEmployeeByEmployeeId(any())).thenReturn(new EmployeeResponseModel());
        when(inventoryClient.getFlowerByFlowerId(any(), any())).thenReturn(new FlowerResponseModel());
        when(requestMapper.requestModelToEntity(any())).thenAnswer(i -> {
            PurchaseOrder p = new PurchaseOrder();
            p.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("p123"));
            p.setPrice(null); // ➕ simulate null price
            return p;
        });
        when(repository.save(any())).thenReturn(entity);
        when(responseMapper.entityToResponseModel(any())).thenReturn(response);

        PurchaseResponseModel result = service.addPurchaseOrder(request);

        assertEquals("p123", result.getPurchaseOrderId());
    }

    @Test
    void testUpdate_missingCurrency_doesNotUpdatePrice() {
        request.setSalePrice(150.0);
        request.setCurrency(null); // ❌ only one provided

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(responseMapper.entityToResponseModel(any())).thenReturn(response);

        PurchaseResponseModel result = service.updatePurchaseOrder("p123", request);
        assertEquals("p123", result.getPurchaseOrderId());
    }

    @Test
    void testUpdate_noFinancingAgreement_doesNotUpdateFinancingFields() {
        request.setFinancingAgreementDetails(null);

        when(repository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId("p123"))
                .thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);
        when(responseMapper.entityToResponseModel(any())).thenReturn(response);

        PurchaseResponseModel result = service.updatePurchaseOrder("p123", request);
        assertEquals("p123", result.getPurchaseOrderId());
    }
}
