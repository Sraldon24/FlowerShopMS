package com.champsoft.services.sales.PresentationLayer;

import com.champsoft.services.sales.BusinessLayer.PurchaseOrderService;
import com.champsoft.services.sales.PresentationLayer.supplierdtos.PhoneType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PurchaseControllerTest {
    @Mock
    private PurchaseOrderService purchaseOrderService;

    @InjectMocks
    private PurchaseController purchaseController;

    private PurchaseRequestModel requestModel;
    private PurchaseResponseModel responseModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestModel = new PurchaseRequestModel(); // Add fields as needed
        responseModel = new PurchaseResponseModel(); // Add fields as needed
    }

    @Test
    public void testAllEnumValues() {
        // This line forces the enum to be fully initialized
        PhoneType[] values = PhoneType.values();

        assertEquals(4, values.length);
        assertEquals(PhoneType.MOBILE, PhoneType.valueOf("MOBILE"));
        assertEquals(PhoneType.HOME, PhoneType.valueOf("HOME"));
        assertEquals(PhoneType.OFFICE, PhoneType.valueOf("OFFICE"));
        assertEquals(PhoneType.WORK, PhoneType.valueOf("WORK"));
    }

    @Test
    public void testEnumToString() {
        assertEquals("MOBILE", PhoneType.MOBILE.toString());
        assertEquals("HOME", PhoneType.HOME.toString());
        assertEquals("OFFICE", PhoneType.OFFICE.toString());
        assertEquals("WORK", PhoneType.WORK.toString());
    }
    @Test
    void testGetAllPurchases() {
        List<PurchaseResponseModel> mockList = Arrays.asList(responseModel);
        when(purchaseOrderService.getAllPurchaseOrders()).thenReturn(mockList);

        List<PurchaseResponseModel> result = purchaseController.getAllPurchases();
        assertEquals(mockList, result);
        verify(purchaseOrderService).getAllPurchaseOrders();
    }

    @Test
    void testGetPurchaseById() {
        String purchaseId = "123";
        when(purchaseOrderService.getPurchaseOrderById(purchaseId)).thenReturn(responseModel);

        PurchaseResponseModel result = purchaseController.getPurchaseById(purchaseId);
        assertEquals(responseModel, result);
        verify(purchaseOrderService).getPurchaseOrderById(purchaseId);
    }

    @Test
    void testCreatePurchase() {
        when(purchaseOrderService.addPurchaseOrder(requestModel)).thenReturn(responseModel);

        PurchaseResponseModel result = purchaseController.createPurchase(requestModel);
        assertEquals(responseModel, result);
        verify(purchaseOrderService).addPurchaseOrder(requestModel);
    }

    @Test
    void testUpdatePurchase() {
        String purchaseId = "123";
        when(purchaseOrderService.updatePurchaseOrder(purchaseId, requestModel)).thenReturn(responseModel);

        PurchaseResponseModel result = purchaseController.updatePurchase(purchaseId, requestModel);
        assertEquals(responseModel, result);
        verify(purchaseOrderService).updatePurchaseOrder(purchaseId, requestModel);
    }

    @Test
    void testDeletePurchase() {
        String purchaseId = "123";

        purchaseController.deletePurchase(purchaseId);
        verify(purchaseOrderService).deletePurchaseOrder(purchaseId);
    }
}
