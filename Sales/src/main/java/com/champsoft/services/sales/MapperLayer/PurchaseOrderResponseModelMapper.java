package com.champsoft.services.sales.MapperLayer;

import com.champsoft.services.sales.Client.PaymentsServiceClient;
import com.champsoft.services.sales.DataLayer.Purchase.PurchaseOrder;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderResponseModelMapper {

    @Autowired
    private PaymentsServiceClient paymentsServiceClient;

    public PurchaseResponseModel entityToResponseModel(PurchaseOrder po) {
        if (po == null) return null;

        PurchaseResponseModel m = new PurchaseResponseModel();

        m.setPurchaseOrderId(po.getPurchaseOrderIdentifier() != null
                ? po.getPurchaseOrderIdentifier().getPurchaseId()
                : null);

        m.setInventoryId(po.getInventoryIdentifier() != null
                ? po.getInventoryIdentifier().getInventoryId()
                : null);

        m.setFlowerIdentificationNumber(po.getFlowerIdentifier() != null
                ? po.getFlowerIdentifier().getFlowerNumber()
                : null);

        m.setSupplierId(po.getSupplierIdentifier() != null
                ? po.getSupplierIdentifier().getSupplierId()
                : null);

        m.setEmployeeId(po.getEmployeeIdentifier() != null
                ? po.getEmployeeIdentifier().getEmployeeId()
                : null);

        /* ─── NEW: paymentId ───────────────────────────── */
        m.setPaymentId(po.getPaymentIdentifier() != null
                ? po.getPaymentIdentifier().getPaymentId()
                : null);

//        /* ─── NEW: paymentDetails ──────────────────────── */
//        if (po.getPaymentIdentifier() != null && po.getPaymentIdentifier().getPaymentId() != null) {
//            try {
//                m.setPaymentDetails(paymentsServiceClient.getPaymentById(
//                        po.getPaymentIdentifier().getPaymentId()));
//            } catch (Exception e) {
//                m.setPaymentDetails(null); // fail safe
//            }
//        }

        /* price & misc */
        m.setSalePrice(po.getPrice() != null ? po.getPrice().getAmount() : null);
        m.setCurrency(po.getPrice() != null && po.getPrice().getCurrency() != null
                ? po.getPrice().getCurrency().toString()
                : null);

        m.setSaleOfferDate(po.getSaleOfferDate());
        m.setSalePurchaseStatus(po.getSalePurchaseStatus());
        m.setFinancingAgreementDetails(po.getFinancingAgreementDetails());

        return m;
    }

    public List<PurchaseResponseModel> entityToResponseModelList(List<PurchaseOrder> entities) {
        return entities != null
                ? entities.stream().map(this::entityToResponseModel).collect(Collectors.toList())
                : null;
    }
}
