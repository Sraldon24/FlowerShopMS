package com.champsoft.services.sales.DataLayer.Purchase;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancingAgreementDetails {
    private Integer numberOfMonthlyPayments;
    private Double monthlyPaymentAmount;
    private Double downPaymentAmount;
}

