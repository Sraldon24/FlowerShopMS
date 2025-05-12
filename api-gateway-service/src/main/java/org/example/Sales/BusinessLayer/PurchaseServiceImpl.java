package org.example.Sales.BusinessLayer;

import lombok.RequiredArgsConstructor;
import org.example.Sales.DomainClientLayer.PurchaseServiceClient;
import org.example.Sales.PresentationLayer.PurchaseRequestModel;
import org.example.Sales.PresentationLayer.PurchaseResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl {

    private final PurchaseServiceClient client;

    public List<PurchaseResponseModel> getAllPurchases() {
        return client.getAllPurchases();
    }

    public PurchaseResponseModel getPurchaseById(String purchaseId) {
        return client.getPurchaseById(purchaseId);
    }

    public PurchaseResponseModel createPurchase(PurchaseRequestModel model) {
        return client.createPurchase(model);
    }

    public PurchaseResponseModel updatePurchase(String purchaseId, PurchaseRequestModel model) {
        return client.updatePurchase(purchaseId, model);
    }

    public void deletePurchase(String purchaseId) {
        client.deletePurchase(purchaseId);
    }
}
