package com.champsoft.services.sales.DataLayer.Purchase;

import com.champsoft.services.sales.DataLayer.Identifiers.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Maps "purchase_id" column
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "purchaseId",
                    column = @Column(name = "purchase_id", nullable = false)
            )
    })
    private PurchaseOrderIdentifier purchaseOrderIdentifier = new PurchaseOrderIdentifier();

    // Maps "sale_status" column
    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status")
    private PurchaseStatus salePurchaseStatus;

    // Maps financing columns: number_of_monthly_payments, monthly_payment_amount, down_payment_amount
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numberOfMonthlyPayments", column = @Column(name = "number_of_monthly_payments")),
            @AttributeOverride(name = "monthlyPaymentAmount", column = @Column(name = "monthly_payment_amount")),
            @AttributeOverride(name = "downPaymentAmount", column = @Column(name = "down_payment_amount"))
    })
    private FinancingAgreementDetails financingAgreementDetails;

    // Maps "sale_offer_date" column
    @Column(name = "sale_offer_date")
    private LocalDate saleOfferDate;

    // Maps "amount" and "currency" columns
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency"))
    })
    private Price price = new Price();


    // Maps "inventory_id" column
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "inventoryId", column = @Column(name = "inventory_id"))
    })
    private InventoryIdentifier inventoryIdentifier;

    // Maps "flower_id" column
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "flowerNumber", column = @Column(name = "flower_id"))
    })
    private FlowerIdentifier flowerIdentifier = new FlowerIdentifier();

    // Maps "supplier_id" column
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "supplierId", column = @Column(name = "supplier_id"))
    })
    private SupplierIdentifier supplierIdentifier;

    // Maps "employee_id" column
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "employeeId", column = @Column(name = "employee_id"))
    })
    private EmployeeIdentifier employeeIdentifier;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "paymentId", column = @Column(name = "payment_id"))
    })
    private PaymentIdentifier paymentIdentifier;   // ✅ new field
}
