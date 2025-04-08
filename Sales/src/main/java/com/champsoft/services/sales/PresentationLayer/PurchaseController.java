package com.champsoft.services.sales.PresentationLayer;

import com.champsoft.services.sales.BusinessLayer.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseOrderService purchaseOrderService;

    // GET /api/v1/purchases
    @GetMapping
    public List<PurchaseResponseModel> getAllPurchases() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    // GET /api/v1/purchases/{purchaseId}
    @GetMapping("/{purchaseId}")
    public PurchaseResponseModel getPurchaseById(@PathVariable String purchaseId) {
        return purchaseOrderService.getPurchaseOrderById(purchaseId);
    }

    // POST /api/v1/purchases
    @PostMapping
    public PurchaseResponseModel createPurchase(@RequestBody PurchaseRequestModel purchaseRequestModel) {
        return purchaseOrderService.addPurchaseOrder(purchaseRequestModel);
    }

    // PUT /api/v1/purchases/{purchaseId}
    @PutMapping("/{purchaseId}")
    public PurchaseResponseModel updatePurchase(
            @PathVariable String purchaseId,
            @RequestBody PurchaseRequestModel purchaseRequestModel
    ) {
        return purchaseOrderService.updatePurchaseOrder(purchaseId, purchaseRequestModel);
    }

    // DELETE /api/v1/purchases/{purchaseId}
    @DeleteMapping("/{purchaseId}")
    public void deletePurchase(@PathVariable String purchaseId) {
        purchaseOrderService.deletePurchaseOrder(purchaseId);
    }
}
