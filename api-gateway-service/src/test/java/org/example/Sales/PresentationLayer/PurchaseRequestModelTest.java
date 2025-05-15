package org.example.Sales.PresentationLayer;

import org.example.Sales.DataLayer.FinancingAgreementDetails;
import org.example.Sales.DataLayer.PurchaseStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseRequestModelTest {

    @Test
    void testEqualsAndHashCode() {
        PurchaseRequestModel model1 = getSampleModel();
        PurchaseRequestModel model2 = getSampleModel();

        assertThat(model1).isEqualTo(model2);
        assertThat(model1.hashCode()).isEqualTo(model2.hashCode());
    }

    @Test
    void testToString() {
        PurchaseRequestModel model = getSampleModel();
        String result = model.toString();
        assertThat(result).contains("purchaseOrderId");
    }

    @Test
    void testCanEqual() {
        PurchaseRequestModel model = getSampleModel();
        assertThat(model.canEqual(new PurchaseRequestModel())).isTrue();
    }

    private PurchaseRequestModel getSampleModel() {
        return PurchaseRequestModel.builder()
                .purchaseOrderId("PO123")
                .inventoryId("INV001")
                .flowerIdentificationNumber("FLOW123")
                .supplierId("SUPP001")
                .employeeId("EMP001")
                .paymentId("PAY001")
                .salePrice(100.0)
                .currency("CAD")
                .payment_currency("USD")
                .saleOfferDate(LocalDate.now())
                .salePurchaseStatus(PurchaseStatus.PENDING)
                .financingAgreementDetails(new FinancingAgreementDetails())
                .build();
    }


}
