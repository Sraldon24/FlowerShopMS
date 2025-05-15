package org.example.ControllerTests;

import org.example.Sales.BusinessLayer.PurchaseServiceImpl;
import org.example.Sales.PresentationLayer.PurchaseController;
import org.example.Sales.PresentationLayer.PurchaseRequestModel;
import org.example.Sales.PresentationLayer.PurchaseResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControllerTests {
    private PurchaseServiceImpl purchaseService;
    private PurchaseController controller;

    @BeforeEach
    void setUp() {
        purchaseService = mock(PurchaseServiceImpl.class);
        controller = new PurchaseController(purchaseService);
    }

    @Test
    void getAllPurchases_shouldReturnList() {
        PurchaseResponseModel model = new PurchaseResponseModel();
        when(purchaseService.getAllPurchases()).thenReturn(Collections.singletonList(model));

        var result = controller.getAllPurchases();
        assertEquals(1, result.size());
    }

    @Test
    void getPurchaseById_shouldReturnOne() {
        PurchaseResponseModel model = new PurchaseResponseModel();
        model.setPurchaseOrderId("PO1");
        when(purchaseService.getPurchaseById("PO1")).thenReturn(model);

        var result = controller.getPurchaseById("PO1");
        assertEquals("PO1", result.getPurchaseOrderId());
    }

    @Test
    void createPurchase_shouldReturnCreated() {
        PurchaseRequestModel req = PurchaseRequestModel.builder().purchaseOrderId("NEW").build();
        PurchaseResponseModel res = new PurchaseResponseModel();
        res.setPurchaseOrderId("NEW");

        when(purchaseService.createPurchase(req)).thenReturn(res);

        var result = controller.createPurchase(req);
        assertEquals("NEW", result.getPurchaseOrderId());
    }

    @Test
    void updatePurchase_shouldReturnUpdated() {
        PurchaseRequestModel req = PurchaseRequestModel.builder().purchaseOrderId("UPD").build();
        PurchaseResponseModel res = new PurchaseResponseModel();
        res.setPurchaseOrderId("UPD");

        when(purchaseService.updatePurchase("UPD", req)).thenReturn(res);

        var result = controller.updatePurchase("UPD", req);
        assertEquals("UPD", result.getPurchaseOrderId());
    }

    @Test
    void deletePurchase_shouldCallService() {
        doNothing().when(purchaseService).deletePurchase("DEL");

        controller.deletePurchase("DEL");

        verify(purchaseService, times(1)).deletePurchase("DEL");
    }
}
