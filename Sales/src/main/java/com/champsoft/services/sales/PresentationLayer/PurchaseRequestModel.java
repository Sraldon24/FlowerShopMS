package com.champsoft.services.sales.PresentationLayer;

import com.champsoft.services.sales.DataLayer.Purchase.FinancingAgreementDetails;
import com.champsoft.services.sales.DataLayer.Purchase.PurchaseStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PurchaseRequestModel {

    private String purchaseOrderId;

    private String inventoryId;
    private String flowerIdentificationNumber; // was "vehicleIdentificationNumber"
    private String supplierId;                 // was "customerId"
    private String employeeId;

    // Pricing
    private Double salePrice;
    private String currency;          // e.g., "CAD"
    private String payment_currency;  // e.g., "USD"

    // Date / Status
    private LocalDate saleOfferDate;
    private PurchaseStatus salePurchaseStatus;

    // Embedded objects
    private FinancingAgreementDetails financingAgreementDetails;

}
