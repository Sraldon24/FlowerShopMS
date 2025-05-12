package org.example.Sales.PresentationLayer;

import lombok.RequiredArgsConstructor;
import org.example.Sales.BusinessLayer.PurchaseServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseServiceImpl purchaseService;

    @GetMapping
    public List<PurchaseResponseModel> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/{purchaseId}")
    public PurchaseResponseModel getPurchaseById(@PathVariable String purchaseId) {
        return purchaseService.getPurchaseById(purchaseId);
    }

    @PostMapping
    public PurchaseResponseModel createPurchase(@RequestBody PurchaseRequestModel purchaseRequestModel) {
        return purchaseService.createPurchase(purchaseRequestModel);
    }

    @PutMapping("/{purchaseId}")
    public PurchaseResponseModel updatePurchase(
            @PathVariable String purchaseId,
            @RequestBody PurchaseRequestModel purchaseRequestModel
    ) {
        return purchaseService.updatePurchase(purchaseId, purchaseRequestModel);
    }

    @DeleteMapping("/{purchaseId}")
    public void deletePurchase(@PathVariable String purchaseId) {
        purchaseService.deletePurchase(purchaseId);
    }
}
