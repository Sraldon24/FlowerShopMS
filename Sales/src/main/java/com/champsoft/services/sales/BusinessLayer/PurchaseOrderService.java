/* ===================================
   PurchaseOrderService.java
   =================================== */
package com.champsoft.services.sales.BusinessLayer;

import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;

import java.util.List;

public interface PurchaseOrderService {
    List<PurchaseResponseModel> getAllPurchaseOrders();

    PurchaseResponseModel getPurchaseOrderById(String purchaseId);

    PurchaseResponseModel addPurchaseOrder(PurchaseRequestModel requestModel);

    PurchaseResponseModel updatePurchaseOrder(String purchaseId, PurchaseRequestModel requestModel);

    void deletePurchaseOrder(String purchaseId);
}
