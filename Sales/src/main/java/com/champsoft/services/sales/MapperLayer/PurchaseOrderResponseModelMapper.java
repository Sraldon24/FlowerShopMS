package com.champsoft.services.sales.MapperLayer;

import com.champsoft.services.sales.DataLayer.Purchase.PurchaseOrder;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderResponseModelMapper {

    default PurchaseResponseModel entityToResponseModel(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) return null;

        PurchaseResponseModel model = new PurchaseResponseModel();

        model.setPurchaseOrderId(purchaseOrder.getPurchaseOrderIdentifier() != null
                ? purchaseOrder.getPurchaseOrderIdentifier().getPurchaseId()
                : null);

        model.setInventoryId(purchaseOrder.getInventoryIdentifier() != null
                ? purchaseOrder.getInventoryIdentifier().getInventoryId()
                : null);

        model.setFlowerIdentificationNumber(purchaseOrder.getFlowerIdentifier() != null
                ? purchaseOrder.getFlowerIdentifier().getFlowerNumber()
                : null);

        model.setSupplierId(purchaseOrder.getSupplierIdentifier() != null
                ? purchaseOrder.getSupplierIdentifier().getSupplierId()
                : null);

        model.setEmployeeId(purchaseOrder.getEmployeeIdentifier() != null
                ? purchaseOrder.getEmployeeIdentifier().getEmployeeId()
                : null);

        model.setSalePrice(purchaseOrder.getPrice() != null ? purchaseOrder.getPrice().getAmount() : null);
        model.setCurrency(purchaseOrder.getPrice() != null && purchaseOrder.getPrice().getCurrency() != null
                ? purchaseOrder.getPrice().getCurrency().toString()
                : null);

        model.setSaleOfferDate(purchaseOrder.getSaleOfferDate());
        model.setSalePurchaseStatus(purchaseOrder.getSalePurchaseStatus());
        model.setFinancingAgreementDetails(purchaseOrder.getFinancingAgreementDetails());

        return model;
    }

    List<PurchaseResponseModel> entityToResponseModelList(List<PurchaseOrder> purchaseOrders);
}
