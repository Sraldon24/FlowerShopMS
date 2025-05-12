package org.example.Sales.PresentationLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.Sales.DataLayer.FinancingAgreementDetails;
import org.example.Sales.DataLayer.PurchaseStatus;
import org.example.Sales.PresentationLayer.inventorydtos.FlowerResponseModel;
import org.example.Sales.PresentationLayer.inventorydtos.InventoryResponseModel;
import org.example.Sales.PresentationLayer.paymentdtos.PaymentResponseModel;
import org.example.Sales.PresentationLayer.supplierdtos.SupplierResponseModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PurchaseResponseModel {

    private String purchaseOrderId;
    private String inventoryId;
    private InventoryResponseModel inventoryDetails;  // 🆕

    private String flowerIdentificationNumber;// formerly vehicleIdentificationNumber
    private FlowerResponseModel flowerDetails;        // 🆕

    private String supplierId;
    private SupplierResponseModel supplierDetails; // ✅ NEW

    // formerly customerId
    private String employeeId;


    private String paymentId;                        // ✅ ADD
    private PaymentResponseModel paymentDetails;   // ✅ ADD (optional enrichment)


    private BigDecimal salePrice;
    private String currency;           // e.g., "CAD"

    private LocalDate saleOfferDate;
    private PurchaseStatus salePurchaseStatus;
    private FinancingAgreementDetails financingAgreementDetails;
}
