package org.example.Sales.PresentationLayer;

import lombok.*;
import org.example.Sales.DataLayer.FinancingAgreementDetails;
import org.example.Sales.DataLayer.PurchaseStatus;

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
    private String paymentId; // ✅ ADD THIS


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
